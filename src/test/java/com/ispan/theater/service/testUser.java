package com.ispan.theater.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ispan.theater.domain.User;
import com.ispan.theater.repository.UserRepository;

@SpringBootTest
public class testUser {

	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	
	@Test
    public void testInsertUser() {
		JSONObject userJson = new JSONObject()
				.put("userFirstname","1111")
				.put("userLastname","小名")
				.put("password","2225")
				.put("email","3331@gmail.com")
				.put("phone","09811787165")
				.put("birth","1999-01-06")
				.put("gender","M");
		 System.out.print(userJson);
		 User user = userService.InsertUser(userJson);
	}
//	@Test
	public void testFindByEmailOrPhone() {
		User user = userRepository.findByEmailOrPhone("email", "phone");
		System.out.println(user);
	}
	
//	@Test
	public void testUserUpdate() {
		JSONObject userJson = new JSONObject()
				.put("id", "1")
				.put("userFirstname","777")
				.put("userLastname","小名")
				.put("password","6666")
				.put("email","333@gmail.com")
				.put("phone","0981787165")
				.put("birth","1988-01-06")
				.put("gender","M")
				.put("isverified","false");
		User user = userService.updateUser(userJson);
	}
//	@Test
	public void testDeleteUser() {
		boolean	result = userService.deleteByUserId(1);
		System.out.println(result);
		
	}
	
//	@Test
	public void testLogin() {
		JSONObject userJson = new JSONObject()
				.put("userName", "3331@gmail.com")
				.put("password", "2225");
		System.out.println(userService.checkLogin(userJson).getId());
		
	}
	@Test
	public void testExistByEmail() {
		System.out.println(userService.existByEmail("3331@gmail.com"));
	}
	
	
	
	
	

}
