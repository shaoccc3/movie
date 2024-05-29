package com.ispan.theater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestRoutingController {
	@GetMapping("/test/user-resign")
	public String loginAjx() {
		return "test/user-resign";
	}

}
