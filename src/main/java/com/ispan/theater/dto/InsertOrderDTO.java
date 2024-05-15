package com.ispan.theater.dto;

import java.util.List;

public class InsertOrderDTO {

	private List<Integer> ticketId;
	private Integer userId;
	private Integer movieId;
	
	public InsertOrderDTO() {
		super();
	}

	public List<Integer> getTicketId() {
		return ticketId;
	}

	public void setTicketId(List<Integer> ticketId) {
		this.ticketId = ticketId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	@Override
	public String toString() {
		return "InsertOrderDTO [ticketId=" + ticketId + ", userId=" + userId + ", movieId=" + movieId + "]";
	}

}
