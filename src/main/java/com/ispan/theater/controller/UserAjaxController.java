package com.ispan.theater.controller;

import java.time.LocalDate;
import java.util.Collections;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ispan.theater.DTO.UserDTO;
import com.ispan.theater.domain.User;
import com.ispan.theater.service.UserService;
import com.ispan.theater.util.EmailSenderComponent;
import com.ispan.theater.util.JsonWebTokenUtility;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping(value =  "/user")
public class UserAjaxController {
	
	
	
	@Autowired
	EmailSenderComponent emailSenderComponent;
	
	@Autowired
	UserService userService;

	@Autowired
	JsonWebTokenUtility jsonWebTokenUtility;

	@PostMapping("/register") // testpage
	public String userRegister(@RequestBody String json) {
		JSONObject repJson = new JSONObject();
		JSONObject userJson = new JSONObject(json);
		System.out.println(userJson.getString("email"));
		Boolean check = userService.existByPhoneOrEmail(userJson.getString("email"), userJson.getString("phone"));
		if (check) {
			repJson.put("success", false);
			repJson.put("message", "新增失敗，資料重複");
			return repJson.toString();
		}
		User user = userService.InsertUser(userJson);
		if (user != null) {
			
//			寄送驗證信
//			JSONObject inputjson = new JSONObject().put("userid", user.getId());
//			String token = jsonWebTokenUtility.createEncryptedToken(inputjson.toString(), null);
//			emailSenderComponent.sendEmail(user.getEmail(),token);

			
			repJson.put("success", true);
			repJson.put("message", "新增成功");
		} else {
			repJson.put("success", false);
			repJson.put("message", "新增失敗");
		}
		return repJson.toString();
	}

	@PostMapping("/login")
	public String userLogin(@RequestBody String json, HttpSession session) {
		JSONObject jsonobj = new JSONObject(json);
		JSONObject result = new JSONObject();
		User user = userService.checkLogin(jsonobj);
		if (user != null) {
			result.put("success", true);
			result.put("message", "登入成功");
			session.setAttribute("id", user.getId());
			JSONObject inputjson = new JSONObject().put("userid", user.getId()).put("email", user.getEmail())
					.put("birth", user.getBirth());
			String token = jsonWebTokenUtility.createEncryptedToken(inputjson.toString(), null);
			result.put("token", token);
			result.put("username", user.getUserFirstname() + user.getUserLastname());

		} else {
			result.put("success", false);
			result.put("message", "登入失敗");
		}
		return result.toString();
	}

	@GetMapping("/check/phone")
	public String checkPhone(@RequestParam String phone) {
		JSONObject result = new JSONObject();
		if (phone == null || phone.length() != 10) {
			result.put("success", false);
			result.put("message", "手機號格式有誤");
		} else {
			if (userService.existByPhone(phone)) {
				result.put("success", false);
				result.put("message", "手機號已存在,請使用其他手機號");
			} else {
				result.put("success", true);
				result.put("message", "手機號可以使用");
			}
		}
		return result.toString();
	}

	@GetMapping("/check/email")
	public String checkEmail(@RequestParam String email) {
		JSONObject result = new JSONObject();
		if (email == null || email.length() == 0) {
			result.put("success", false);
			result.put("message", "Email格式有誤");
		} else {
			if (userService.existByEmail(email)) {
				result.put("success", false);
				result.put("message", "Email已存在,請使用其他Eamil");
			} else {
				result.put("success", true);
				result.put("message", "Eamil可以使用");
			}
		}
		return result.toString();
	}

	@GetMapping("/profile")
	public ResponseEntity<?> userProfile(@RequestParam String token) {

		String data = jsonWebTokenUtility.validateEncryptedToken(token);
		if (data != null && data.length() != 0) {
			JSONObject obj = new JSONObject(data);
			Integer id = obj.getInt("userid");
			User user = userService.getUserById(id);
			if (user != null) {
				ResponseEntity<User> ok = ResponseEntity.ok(user);
				return ok;
			}
		}
		ResponseEntity<Void> notFound = ResponseEntity.notFound().build();
		return notFound;
	}

	@PostMapping("/login/google")
	public String testGoolgle(@RequestBody String credentialJSON) {
		String credential = new JSONObject (credentialJSON).getString("credential");
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
				// Specify the CLIENT_ID of the app that accesses the backend:
				.setAudience(Collections
						.singletonList("655138938513-u7u4vrhur12fmjujfubctji8eflv4usn.apps.googleusercontent.com"))
				// Or, if multiple clients access the backend:
				// .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
				.build();
		JSONObject result = new JSONObject();
		// (Receive idTokenString by HTTPS POST)
		try {
			GoogleIdToken idToken = verifier.verify(credential);
			if (idToken != null) {
				Payload payload = idToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();
				// Get profile information from payload
				String email = payload.getEmail();
				// 可 透過URL下載客戶圖片上傳DB
				String pictureUrl = (String) payload.get("picture");
				String familyName = (String) payload.get("family_name");
				String givenName = (String) payload.get("given_name");
				// 有註冊過
				User dbuser = userService.findUserByEmail(email);
				if (dbuser != null) {
					JSONObject inputjson = new JSONObject().put("userid", dbuser.getId())
							.put("email", dbuser.getEmail()).put("birth", dbuser.getBirth());
					String token = jsonWebTokenUtility.createEncryptedToken(inputjson.toString(), null);
					result.put("token", token);
					result.put("username", dbuser.getUserFirstname() + dbuser.getUserLastname());
					result.put("message", "歡迎回來");
					result.put("success", true);
				} else {// 沒註冊過
					JSONObject insertUser = new JSONObject().put("userFirstname", familyName)
							.put("userLastname", givenName).put("email", email).put("password", userId)
							.put("phone", "09" + userId.substring(0, 8)).put("birth", LocalDate.now().toString())
							.put("gender", "M").put("isverified", true).put("image", "");
					User user = userService.InsertUser(insertUser);
					if (user != null) {
						result.put("success", true);
						result.put("message", "新增成功");
						JSONObject inputjson = new JSONObject().put("userid", user.getId())
								.put("email", user.getEmail()).put("birth", user.getBirth());
						String token = jsonWebTokenUtility.createEncryptedToken(inputjson.toString(), null);
						result.put("token", token);
						result.put("username", user.getUserFirstname() + user.getUserLastname());
					} else {
						result.put("success", false);
						result.put("message", "新增失敗");
					}
				}
			} else {
				result.put("success", false);
				result.put("message", "Invalid ID token.請重新操作");
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			return result.toString();
		}

	}

	
	
	//修改密碼  未完成
	@PutMapping("/check/changePaaword/{token}")
	public void changePaaword(@PathVariable(name = "token") 
	
	String token, @RequestBody String password) {
		String data = jsonWebTokenUtility.validateEncryptedToken(token);
		if (data != null && data.length() != 0) {
			JSONObject obj = new JSONObject(data);
			Integer userid = obj.getInt("userid");
			JSONObject update = new JSONObject(password).put("userid", userid);
			userService.updateUser(update);
		}

	}
	
	
	//修改個人資料 (要跟修改密碼做區別) 未完成
	@PutMapping("/check/changeUserProfile/{token}")
	public void changeUserProfile(@PathVariable(name = "token") String token, @RequestBody UserDTO userDTO) {
		String data = jsonWebTokenUtility.validateEncryptedToken(token);
		if (data != null && data.length() != 0) {
			Integer userid = new JSONObject(data).getInt("userid");
			JSONObject update = new JSONObject(userDTO).put("userid", userid);
			
			userService.updateUser(update);
		}

	}
	
	
	
	
	//Email驗證
	@PutMapping("/verify-email/{token}")
	public String testuser(@PathVariable(name = "token") String token) {
		System.out.println("sendokokok");
		JSONObject result =new JSONObject();
		//檢查token並解析
		String data = jsonWebTokenUtility.validateEncryptedToken(token);
		//tokern有效
		if(data != null && data.length() != 0) {
			Integer userid = new JSONObject(data).getInt("userid");
			JSONObject update = new JSONObject().put("userid", userid).put("isverified", true);
			userService.updateUser(update);
			result.put("success", true);
			result.put("message", "郵件驗證成功");
			
		}else {
			result.put("success", false);
			result.put("message", "郵件驗證失敗，請重新獲取驗證信進行驗證");
		}
		return result.toString();
	}
	

}
