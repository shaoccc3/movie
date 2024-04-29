package com.ispan.theater.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.User;
import com.ispan.theater.service.UserService;

@RestController
@CrossOrigin
public class UserAjaxController {
	@Autowired
	UserService userService;

	@PostMapping("user/register") // testpage
	public String userRegister(@RequestBody String json) {
		JSONObject repJson = new JSONObject();
		JSONObject userJson = new JSONObject(json);
		String email = userJson.isNull("email") ? null : userJson.getString("email");
		String phone = userJson.isNull("phone") ? null : userJson.getString("phone");
		String password = userJson.isNull("password") ? null : userJson.getString("password");
		User user = userService.InsertUser(userJson);
		if (user != null) {
			repJson.put("success", "true");
			repJson.put("message", "新增成功");
		}else {
			repJson.put("success", "false");
			repJson.put("message", "新增失敗");
		}
		return repJson.toString();
	}

}
