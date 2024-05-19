package com.ispan.theater.controller;

import java.util.Map;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TestController {

	@PostMapping("/order-redirect")
	public String redirectFront(HttpServletRequest request) {
		System.out.println(request.getParameter("RtnCode"));
		System.out.println(request.getParameter("MerchantTradeNo"));
		return "redirect:"+"http://localhost:5173/order/showtimes";
	}
	
}
