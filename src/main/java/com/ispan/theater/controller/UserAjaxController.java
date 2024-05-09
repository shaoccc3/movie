package com.ispan.theater.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.User;
import com.ispan.theater.service.UserService;
import com.ispan.theater.util.JsonWebTokenUtility;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class UserAjaxController {
	@Autowired
	UserService userService;
	
	@Autowired
	JsonWebTokenUtility jsonWebTokenUtility;

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
	public String userLogin(@RequestBody String  json,HttpSession session) {
		JSONObject jsonobj =new JSONObject(json);
		JSONObject result = new JSONObject();
		User user = userService.checkLogin(jsonobj);
		if (user!=null) {
			result.put("success", true);
			result.put("message", "登入成功");
			session.setAttribute("id", user.getId());
			JSONObject inputjson= new JSONObject()
					.put("userid", user.getId())
					.put("email",user.getEmail())
					.put("birth", user.getBirth());
			String token = jsonWebTokenUtility.createEncryptedToken(inputjson.toString(),null);
			result.put("token", token);
			result.put("username", user.getUserFirstname()+user.getUserLastname());
			
		}else{
			result.put("success", false);
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
	
	
	@GetMapping("/user/check/email")
	public String checkEmail(@RequestParam String email) {
		JSONObject result=new JSONObject();
		if(email==null||email.length()!=0) {
			result.put("success", false);
			result.put("message", "Email格式有誤");
		}else {
			if(userService.existByEmail(email)) {
				result.put("success", false);
				result.put("message", "Email已存在,請使用其他Eamil");
			}else {
				result.put("success", true);
				result.put("message", "Eamil可以使用");	
			}
		}
		return result.toString();
	}
	
	
	
	
	@GetMapping("/user/profile")
	public ResponseEntity<?> userProfile(@RequestParam String token) {
		
		String data = jsonWebTokenUtility.validateEncryptedToken(token);
		if(data!=null&&data.length()!=0) {
			JSONObject obj =  new JSONObject(data);
			Integer id = obj.getInt("userid");
			User user = userService.getUserById(id);
			if(user!=null) {
				ResponseEntity<User> ok=  ResponseEntity.ok(user);
				return ok;
			}
		}
			ResponseEntity<Void> notFound = ResponseEntity.notFound().build();
			return notFound;
	}
	
	
	
	
	
	
	
	
}
