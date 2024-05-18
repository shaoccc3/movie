package com.ispan.theater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {

	@PostMapping("/order-redirect")
	public String redirectFront() {
		return "redirect:"+"http://localhost:5173/order/showtimes";
	}
	
}
