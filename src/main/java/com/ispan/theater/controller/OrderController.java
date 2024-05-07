package com.ispan.theater.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Order;
import com.ispan.theater.service.OrderService;

@RestController
public class OrderController {
	@Autowired
	OrderService os;
	
	@GetMapping("/movie/order")
	public String getOrderById(@RequestParam(name="id")Integer id) {
		JSONObject orderJson=new JSONObject();
		Order order=os.getOrderById(id);
		if(order==null) {
			return orderJson.put("fail",false).toString();
		}
		return orderJson.put("id",order.getId()).put("createDate", order.getCreateDate()).put("modifyDate", order.getModifyDate()).
				put("orderAmount", order.getOrderAmount()).put("movieId", order.getMovie().getId()).put("userId", order.getUser().getId()).put("seccess", true).toString();
	}
	
	@GetMapping("/movie/orders")
	public String getOrders() {
		List<Order> orders=os.getOrders();
		System.out.println(orders);
		JSONObject orderJson=new JSONObject();
		if(orders.size()==0) {
			return orderJson.put("fail",false).toString();
		}
		JSONArray array=new JSONArray();
		orders.stream().forEach((order)->{
			JSONObject temp=new JSONObject();
			temp.put("id", order.getId()).put("createDate", order.getCreateDate()).
			put("modifyDate",order.getModifyDate()).put("orderAmount", order.getOrderAmount());
			array.put(temp);
		});
		return orderJson.put("orders", array).toString();
	}
	
}
