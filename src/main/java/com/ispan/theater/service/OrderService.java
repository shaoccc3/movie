package com.ispan.theater.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.theater.domain.Order;
import com.ispan.theater.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository or;
	
	public Order getOrderById(Integer id) {
		Optional<Order> order=or.findById(id);
		return order.orElse(null);
	}
	
	public List<Order> getOrders(){
		return or.findAll();
	}
}
