package com.ispan.theater.service;

import java.util.Date;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ispan.theater.domain.Food;
import com.ispan.theater.repository.FoodRepository;
import com.ispan.theater.util.DatetimeConverter;

@SpringBootTest
public class testFoodService {
	
	@Autowired
	private FoodRepository foodRepository;
	@Autowired
	private FoodService foodService;
	
	
	public void testModifyFood() {
		
		
	}
	
//	@Test
	public void testCreateFood() {
		
		Food newFood = new Food();
        newFood.setId(1);
        newFood.setName("爆米花");
        newFood.setPrice(140.0);
        newFood.setCount(100);
        newFood.setCreateDate(DatetimeConverter.parse("2024-06-01", "yyyy-MM-dd"));
        newFood.setModifyDate(DatetimeConverter.parse("2024-06-01", "yyyy-MM-dd"));
		
        Food newFood2 = new Food();
        newFood2.setId(2);
        newFood2.setName("熱狗堡");
        newFood2.setPrice(80.0);
        newFood2.setCount(90);
        newFood2.setCreateDate(new Date());
        newFood2.setModifyDate(new Date());
        
        Food newFood3 = new Food();
        newFood3.setId(3);
        newFood3.setName("吉拿棒");
        newFood3.setPrice(70.0);
        newFood3.setCount(90);
        newFood3.setCreateDate(new Date());
        newFood3.setModifyDate(new Date());
        
        Food newFood4 = new Food();
        newFood4.setId(4);
        newFood4.setName("玉米");
        newFood4.setPrice(60.0);
        newFood4.setCount(85);
        newFood4.setCreateDate(new Date());
        newFood4.setModifyDate(new Date());
        
        Food newFood5 = new Food();
        newFood5.setId(5);
        newFood5.setName("雪碧");
        newFood5.setPrice(40.0);
        newFood5.setCount(50);
        newFood5.setCreateDate(new Date());
        newFood5.setModifyDate(new Date());
        
        Food newFood6 = new Food();
        newFood6.setId(6);
        newFood6.setName("可樂");
        newFood6.setPrice(40.0);
        newFood6.setCount(50);
        newFood6.setCreateDate(new Date());
        newFood6.setModifyDate(new Date());
        
        Food newFood7 = new Food();
        newFood7.setId(7);
        newFood7.setName("激浪");
        newFood7.setPrice(40.0);
        newFood7.setCount(50);
        newFood7.setCreateDate(new Date());
        newFood7.setModifyDate(new Date());
		
        System.out.println("newfood: " + newFood);
        foodRepository.save(newFood);
        foodRepository.save(newFood2);
        foodRepository.save(newFood3);
        foodRepository.save(newFood4);
        foodRepository.save(newFood5);
        foodRepository.save(newFood6);
        foodRepository.save(newFood7);
	}

}
