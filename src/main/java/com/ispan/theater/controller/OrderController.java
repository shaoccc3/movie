package com.ispan.theater.controller;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Order;
import com.ispan.theater.domain.Ticket;
import com.ispan.theater.repository.TicketRepository;
import com.ispan.theater.service.OrderService;

import jakarta.transaction.Transactional;

@RestController
public class OrderController {
	@Autowired
	OrderService os;
	@Autowired
	TicketRepository tr;
	@GetMapping("/movie/order")
//	@Cacheable(cacheNames = "Order",key="#id")
	public String getOrderById(@RequestParam(name="id")Integer id) {
		JSONObject orderJson=new JSONObject();
		Order order=os.findOrderByOrderId(id);
		if(order==null) {
			return orderJson.put("success",false).toString();
		}
//		System.out.println(order.getOrderDetails());
		return orderJson.put("id",order.getId()).put("createDate", order.getCreateDate()).put("modifyDate", order.getModifyDate()).
				put("orderAmount", order.getOrderAmount()).put("movieId", order.getMovie().getId()).put("userId", order.getUser().getId()).put("success", true).toString();
	}
	
	@GetMapping("/movie/orders")
//	@Cacheable(cacheNames = "Orders")
	public String getOrders() {
		List<Order> orders=os.getOrders();
		System.out.println(orders);
		JSONObject orderJson=new JSONObject();
		if(orders.size()==0) {
			return orderJson.put("success",false).toString();
		}
		JSONArray array=new JSONArray();
		orders.stream().forEach((order)->{
			JSONObject temp=new JSONObject();
			temp.put("id", order.getId()).put("createDate", order.getCreateDate()).
			put("modifyDate",order.getModifyDate()).put("orderAmount", order.getOrderAmount());
			array.put(temp);
		});
		return orderJson.put("orders", array).put("success", true).toString();
	}
	
	@GetMapping("/movie/locktest")
	public String lockTest() {
		Order order=os.findOrderByOrderId(3);
		synchronized (this) {
			if(order.getOrderAmount()==5) {
				return new JSONObject().put("success", false).toString();
			}
			os.updeteOrderAmount(order);
		}
		return new JSONObject().put("success",true).toString();
	}
	
	@GetMapping("/movie/addOrderTest")
	public String addOrderTest(@RequestParam("ticketId")Integer ticketId) {
		Ticket ticket=tr.findById(ticketId).orElse(null);
		synchronized (this) {
			if(!"未售出".equals(ticket.getIsAvailable())) {
				System.out.println(new Date(System.currentTimeMillis())+"未買到");
				return new JSONObject().put("success", false).toString();
			}
			os.setTicket(ticket);
			System.out.println(new Date(System.currentTimeMillis())+"買到");
		}
		return new JSONObject().put("success", true).toString();
	}
	
	
}
