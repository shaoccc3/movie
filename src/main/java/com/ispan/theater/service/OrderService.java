package com.ispan.theater.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.Order;
import com.ispan.theater.domain.Ticket;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.OrderRepository;
import com.ispan.theater.repository.ScreeningRepository;
import com.ispan.theater.repository.TicketRepository;



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
	
	public List<String> findScreeningByDate(Integer auditoriumId){
		return screeningRepository.findScreeningByDate(auditoriumId).stream().map(date->date.toString().substring(0, 10)).collect(Collectors.toList());
	}
	
	public List<Map<String,Object>> findScreeningByTime(Integer auditoriumId,String Date){
		List<Map<String,Object>> list=screeningRepository.findScreeningByTime(auditoriumId, Date);
		for(int i=0;i<list.size();i++) {
			list.set(i,new HashMap<String,Object>(list.get(i)));
			list.get(i).put("Start_time", list.get(i).get("Start_time").toString().substring(11,19));
		}
		return list;
	}
	
	public List<Map<String,Object>> ticketList(Integer id){
		return ticketRepository.getTickets(id);
	}
	
	public List<Ticket> ticketList1(Integer id){
		return ticketRepository.test(id);
	}
	
}
