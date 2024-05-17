package com.ispan.theater.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
		List<Map<String,Object>> list=screeningRepository.findScreeningByDate(cinemaId,movieId);
		for(int i=0;i<list.size();i++) {
			list.set(i,new HashMap<String,Object>(list.get(i)));
			list.get(i).put("Start_time", list.get(i).get("Start_time").toString().substring(0, 10));
		}
		return list;
	}
	
	public List<Map<String,Object>> findScreeningByTime(Integer cinemaId,String Date,Integer movieId){
		List<Map<String,Object>> list=screeningRepository.findScreeningByTime(cinemaId, Date,movieId);
		for(int i=0;i<list.size();i++) {
			list.set(i,new HashMap<String,Object>(list.get(i)));
			list.get(i).put("Start_time", list.get(i).get("Start_time").toString().substring(11,19));
		}
		return list;
	}
	
	public List<Map<String,Object>> ticketList(Integer id){
		return ticketRepository.getTickets(id);
	}
	
	public List<Map<String,Object>> ticketList1(Integer id){
		return ticketRepository.getTickets(id);
	}
	
//	@Transactional
//	public void setTicketAvailable(List<Integer> list) {
//		ticketRepository.setTicketAvailable("已售出", list);
//	}
	
	@Transactional
	public String createOrder(InsertOrderDTO insertOrderDto) {
		String Date=DatetimeConverter.createSqlDatetime(new Date());
		Order order=null;
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
		return linePayService.request(order,insertOrderDto.getTicketId().size()).get("info").get("paymentUrl").get("web");
	}
	@Transactional
	public String orderCompleted(Integer orderId) {
		orderRepository.setOrderConditionByUserId(orderId);
		return new JSONObject().put("Order", orderRepository.orderCompleted(orderId)).toString();
	}
	
}
