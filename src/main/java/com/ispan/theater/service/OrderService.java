package com.ispan.theater.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.Order;
import com.ispan.theater.domain.OrderDetail;
import com.ispan.theater.domain.Ticket;
import com.ispan.theater.dto.InsertOrderDTO;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.OrderDetailRepository;
import com.ispan.theater.repository.OrderRepository;
import com.ispan.theater.repository.ScreeningRepository;
import com.ispan.theater.repository.TicketRepository;
import com.ispan.theater.util.DatetimeConverter;



@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	ScreeningRepository screeningRepository;
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	OrderDetailRepository orderDetailRepository;
	@Autowired
	LinePayService linePayService;
	@Autowired
	ECPayService ecPayService;
	@Autowired
	PaypalService paypalService;
	String paypalcancelUrl = "https://httpbin.org/get?paymentStatus=cancelled";

	@Transactional(isolation=Isolation.READ_COMMITTED)
	public Order findOrderByOrderId(Integer id) {
		Optional<Order> order=orderRepository.findById(id);
		return order.orElse(null);
	}
	
	public Order findOrderByUserId(Integer id) {
		Optional<Order> order=orderRepository.findOrderByUserId(id);
		return order.orElse(null);
	}
	
	public List<Order> getOrders(){
		return orderRepository.findAll();
	}
	
	@Transactional
	public void updeteOrderAmount(Order order) {
		order.setOrderAmount(order.getOrderAmount()+1);
	}
	
	@Transactional
	public void updeteOrderAmountTest() {
		Order order=orderRepository.findOrderByUserId(3).orElse(null);
		order.setOrderAmount(order.getOrderAmount()+1);
	}
	
	@Transactional
	public void setTicket(Ticket ticket) {
		ticket.setIsAvailable("已售出");
	}
	
	public List<Map<String,Object>> findMovieByScreening(Integer cinemaId){
		return movieRepository.findMovieByScreening(cinemaId);
	}
	
	public List<Map<String,Object>> findScreeningByDate(Integer cinemaId,Integer movieId){
		return screeningRepository.findScreeningByDate(cinemaId,movieId);
	}
	
	public List<Map<String,Object>> findScreeningByTime(Integer cinemaId,String Date,Integer movieId){
		return screeningRepository.findScreeningByTime(cinemaId, Date,movieId);
	}
	
	public List<Map<String,Object>> ticketList(Integer id){
		return ticketRepository.getTickets(id);
	}

	
	@Transactional
	public String createOrder(InsertOrderDTO insertOrderDto) throws PayPalRESTException {
		String Date=DatetimeConverter.createSqlDatetime(new Date());
		Order order=null;
		String result="";
		Integer count=orderRepository.createOrder(Date,Date,(300.0*(insertOrderDto.getTicketId().size())),insertOrderDto.getMovieId(),insertOrderDto.getUserId(),0);
		if(count>0) {
			order=orderRepository.findOrderByUserIdAndCreateDate(Date, insertOrderDto.getUserId()).get();
		}
		List<Ticket> tickets=ticketRepository.findTicketsById(insertOrderDto.getTicketId());
		List<OrderDetail> orderDetails=new ArrayList<OrderDetail>();
		for(int i=0;i<tickets.size();i++) {
			orderDetails.add(new OrderDetail(order,tickets.get(i)));
		}
		orderDetailRepository.saveAll(orderDetails);
		for(int i=0;i<tickets.size();i++) {
			if(!"未售出".equals(tickets.get(i).getIsAvailable())){
				return new JSONObject().put("success", false).toString();
			}
		}
		ticketRepository.setTicketAvailable("已售出", insertOrderDto.getTicketId());
		if("linePay".equals(insertOrderDto.getPaymentOptions())) {
			order.setSupplier("linepay");
			result=linePayService.request(order,insertOrderDto.getTicketId().size()).get("info").get("paymentUrl").get("web");
		}
		if("ecPay".equals(insertOrderDto.getPaymentOptions())) {
			String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
			order.setPaymentNo(uuId);
			order.setSupplier("ecpay");
			result=ecPayService.ecpayCheckout(order, insertOrderDto.getTicketId().size());
		}
		if("paypal".equals(insertOrderDto.getPaymentOptions())){
			String paypalsuccessUrl =  "http://localhost:5173/order/paymentsuccess?orderId=" + order.getId();
			try {
				order.setSupplier("paypal");
				Payment payment = paypalService.createPayment(order.getOrderAmount(),"TWD","paypal", "sale", "Payment Description", paypalcancelUrl,
						paypalsuccessUrl);
				for (Links link : payment.getLinks()) {
					if (link.getRel().equals("approval_url")) {
						return link.getHref();
					}
				}
				result =  "No approval URL FOUND";
			} catch (PayPalRESTException e) {
				result ="ERROR: " + e.getMessage();
			}

		}
		return result;
	}
	
	@Transactional
	public String orderCompleted(String transactionId,Integer orderId) {
		System.out.println(linePayService.confirm(transactionId,orderId).get("returnCode"));//returnCode為0000
		orderRepository.setPaymentNoAndConditionByOrderId(transactionId, orderId);
		return new JSONObject().put("Order", orderRepository.orderCompleted(orderId)).toString();
	}
	@Transactional
	public String completePaypalOrder(String paymentId, String payerId, Integer orderId) {
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			String saleId = paypalService.getSaleIdFromPayment(payment);
			if (payment.getState().equals("approved")) {
				orderRepository.setPaymentNoAndConditionByOrderId(saleId, orderId);
				return new JSONObject().put("success", true).put("order", orderRepository.orderCompleted(orderId)).toString();
			}
		} catch (PayPalRESTException e) {
			return new JSONObject().put("success", false).put("error", e.getMessage()).toString();
		}
		return new JSONObject().put("success", false).toString();
	}
	
	public String getOrder(Integer userId,Integer page) {
		Integer total=orderRepository.orderTotalByUserId(userId).get("order_total");
		Integer pages=1;
		if(total%10==0&&total/10!=0) {
			pages=total/10;
		}else if(total%10!=0&&total/10!=0) {
			pages=(total/10)+1;
		}
		return new JSONObject().put("orders", orderRepository.getOrderByUser(userId,(page-1)*10)).put("pages", pages).toString();
	}
	
	public String getOrderDetail(Integer orderId){
		return new JSONObject().put("details", orderRepository.orderCompleted(orderId)).toString();
	}
	public String getTicketfromDetail(Integer orderId){
		return new JSONObject().put("details", orderDetailRepository.getTicketDetail(orderId)).toString();
	}
}
