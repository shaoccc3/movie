package com.ispan.theater.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.CustomerService;
import com.ispan.theater.domain.Employee;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.CustomerServiceRepository;

@Service
@Transactional
public class CustomerServiceService {

	@Autowired
	private CustomerServiceRepository customerServiceRepository;
	@Autowired
	private UserService userService;

	// @Autowired
	// private JsonWebTokenUtility jsonWebTokenUtility;

	// 新增
	// 從token取得使用者id
//	有登入
	public CustomerService insertCustomerService(JSONObject json) {
		Integer userId = json.isNull("userid") ? null : json.getInt("userid");

		String text = json.isNull("text") ? null : json.getString("text");
		String category = json.isNull("category") ? null : json.getString("category");
		String userEmail = json.isNull("userEmail") ? null : json.getString("userEmail");

		CustomerService customerService = new CustomerService();

		if (userId != null && userId != 0) {
			customerService.setUser(userService.getUserById(userId));

		}

		customerService.setText(text);
		customerService.setCategory(category);
		customerService.setUserEmail(userEmail);
		customerService.setStatus("false");
		// customerService.setAgentEmp(null);
		customerService.setCreateDate(new Date());
		customerService.setHandleDate(new Date());

		return customerServiceRepository.save(customerService);

	}

//	沒登入
	public CustomerService insertCustomerServiceNoUser(JSONObject json ) {

		String text = json.isNull("text") ? null : json.getString("text");
		String category = json.isNull("category") ? null : json.getString("category");
		String userEmail = json.isNull("userEmail") ? null : json.getString("userEmail");

		CustomerService customerService = new CustomerService();

		

		customerService.setText(text);
		customerService.setCategory(category);
		customerService.setUserEmail(userEmail);
		customerService.setStatus("false");
		// customerService.setAgentEmp(null);
		customerService.setCreateDate(new Date());
		customerService.setHandleDate(new Date());

		return customerServiceRepository.save(customerService);

	}
	// 客人登入修改
	public CustomerService updateCustomerService(JSONObject json) {

		Integer custserviceId = json.isNull("custserviceId")?null: json.getInt("custserviceId");
		String text = json.isNull("text") ? null : json.getString("text");
		String category = json.isNull("category") ? null : json.getString("category");
		String userEmail = json.isNull("userEmail") ? null : json.getString("userEmail");
		
		
		
		if (custserviceId != null) {
			Optional<CustomerService> optional = customerServiceRepository.findById(custserviceId);
			if (optional.isPresent()) {
				
				CustomerService customerService = optional.get();
				
				
				customerService.setHandleDate(new Date());
				customerService.setText(text);
				customerService.setCategory(category);
				customerService.setUserEmail(userEmail);

				return customerServiceRepository.save(customerService);
			}
		}

		return null;
	}

	public boolean existById(Integer id) {
		if (id != null) {
			return customerServiceRepository.existsById(id);
		}
		return false;
	}

	// 管理員修改
	public CustomerService updateCustomerServiceByEmp(JSONObject json, Employee emp) {
		try {
			Integer id = json.isNull("id") ? null : json.getInt("id");
			String status = json.isNull("status") ? null : json.getString("status");

			if (id != null) {
				Optional<CustomerService> optional = customerServiceRepository.findById(id);
				if (optional.isPresent()) {
					CustomerService customerService = optional.get();
					customerService.setHandleDate(new Date());
					customerService.setStatus(status);
					// customerService.setAgentEmp(emp);

					return customerServiceRepository.save(customerService);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
