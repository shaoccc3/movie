package com.ispan.theater.controller;

import java.net.URI;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ispan.theater.domain.CustomerService;
import com.ispan.theater.service.CustomerServiceService;
import com.ispan.theater.util.JsonWebTokenUtility;
@Controller
@CrossOrigin
public class CustomerServiceAjaxController {
    @Autowired
    private CustomerServiceService customerServiceService;

    @Autowired
    private JsonWebTokenUtility jwtu;
    
//    @Value("${site.path}")
//     private String path;

    // String
    @PostMapping("/customerServices")
    public ResponseEntity<?> createCustomerService(@RequestBody String jsonObject,
    		@RequestHeader("Authorization") String token) {

    	
    	if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 移除 "Bearer " 前綴
            System.out.println(token);
            
        // 取得USERID
    
            // 解碼TOKEN
            String authToken = jwtu.validateToken(token);
            System.out.println(authToken);
            if (authToken != null) {
                // 解碼TOKEN
                JSONObject obj = new JSONObject(authToken);
                Integer userId = obj.getInt("userid");
                System.out.println(userId);

                // 新增

                JSONObject json = new JSONObject(jsonObject);
                json.put("userid", userId);

                if (userId != null) {

                    CustomerService createdCustomerService = customerServiceService.insertCustomerService(json);
                    if (createdCustomerService != null) {
                        // endpoint
                        // @Value("${site.path}")
                        // private String path;

                        String uri = "http://localhost:8080/customerServices/" + createdCustomerService.getId();
                        return ResponseEntity.created(URI.create(uri))
                        		.contentType(MediaType.APPLICATION_JSON)
                                .body(createdCustomerService);

                    }
                }
            
                     
            }else {
            	// 錯誤的TOKEN
	            System.out.println("Ttoken遺失");
			        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ttoken遺失");

            }
            	
            

        }

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomerService(@PathVariable Integer csId,
            @RequestBody JSONObject jsonObject) {
        // id錯誤
        if (csId == null || !customerServiceService.existById(csId)) {
            return ResponseEntity.notFound().build();
        } else {
            // id正確
            CustomerService updatedCustomerService = customerServiceService.updateCustomerService(jsonObject);
            if (updatedCustomerService == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(updatedCustomerService);
            }
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getCustomerServiceById(@PathVariable Integer id) {
//        // id錯誤
//        if (id == null || !customerServiceService.existById(id)) {
//            return ResponseEntity.notFound().build();
//        } else {
//            // id正確
//            CustomerService customerService = customerServiceService.findById(id);
//            if (customerService == null) {
//                return ResponseEntity.notFound().build();
//            } else {
//                return ResponseEntity.ok().body(customerService);
//            }
//
//        }
//    }

}
