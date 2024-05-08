package com.ispan.theater.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.User;
import com.ispan.theater.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class UserAjaxController {
	@Autowired
	UserService userService;

	@PostMapping("/user/register") // testpage
	public String userRegister(@RequestBody String json) {
		JSONObject repJson = new JSONObject();
		JSONObject userJson = new JSONObject(json);
		String email = userJson.isNull("email") ? null : userJson.getString("email");
		String phone = userJson.isNull("phone") ? null : userJson.getString("phone");
		String password = userJson.isNull("password") ? null : userJson.getString("password");
		User user = userService.InsertUser(userJson);
		if (user != null) {
			repJson.put("success", true);
			repJson.put("message", "新增成功");
		}else {
			repJson.put("success", false);
			repJson.put("message", "新增失敗");
		}
		return repJson.toString();
	}
	
	
	
	@PostMapping("/user/login")
	public String userLogin(@RequestBody String json,HttpSession session ) {
		JSONObject jsonobj =new JSONObject(json);
		JSONObject result = new JSONObject();
		User user = userService.checkLogin(jsonobj);
		if (user!=null) {
			session.setAttribute("user", user);
			result.put("message", "登入成功");
		}else{
			result.put("message", "登入失敗");
		}
		return result.toString();
	}
	
	@GetMapping("/user/check/phone")
	public String checkPhone(@RequestParam String phone) {
		JSONObject result=new JSONObject();
		if(phone==null||phone.length()!=10) {
			result.put("success", false);
			result.put("message", "手機號格式有誤");
		}else {
			if(userService.existByPhone(phone)) {
				result.put("success", false);
				result.put("message", "手機號已存在,請使用其他手機號");
			}else {
				result.put("success", true);
				result.put("message", "手機號可以使用");	
			}
		}
		return result.toString();
	}
	
	
	
	
	@GetMapping("/user/profile")
	public String userProfile(HttpSession session) {
		JSONObject result = new JSONObject();
		String id = (String)session.getAttribute("id");
		System.out.println(id);
		User user = userService.getUserById(Integer.parseInt(id));
		result.put("user",user);
		return result.toString();
		
	}
	
	
	
	
	
	
	
	
}
