package com.ispan.theater.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.Order;
import com.ispan.theater.domain.Ticket;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.OrderRepository;



@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	MovieRepository movieRepository;
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
	
	public List<String> findMovieByScreening(Integer cinemaId){
		return movieRepository.findMovieByScreening(cinemaId);
	}
}
