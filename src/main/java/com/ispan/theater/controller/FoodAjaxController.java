package com.ispan.theater.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Food;
import com.ispan.theater.domain.FoodPicture;
import com.ispan.theater.service.FoodService;
import com.ispan.theater.util.DatetimeConverter;

import jakarta.annotation.PostConstruct;

@RestController
@CrossOrigin
public class FoodAjaxController {
	
	@Autowired
	private FoodService foodService;
	
	@GetMapping("/backstage/food/name/{name}")
	public String existsByName(@PathVariable("name") String name) {
		JSONObject responseJson = new JSONObject();
		boolean exist = foodService.existByName(name);
		if (exist) {
			responseJson.put("success", false);
			responseJson.put("message", "名稱存在");
		}else {
			responseJson.put("success", true);
			responseJson.put("message", "名稱不存在");
		}
		return responseJson.toString();
	}
	
	@PostMapping("/backstage/food")//postman test
	public String createFood (@RequestBody String foodjson) {
		JSONObject response = new JSONObject();
		JSONObject jsonObject = new JSONObject(foodjson);
		String name = jsonObject.isNull("name")? null : jsonObject.getString("name");
		if(foodService.existsFoodByName(name)) {
			response.put("success", false);
			response.put("message", "name已存在");
		}else {
			Food food = foodService.createFood(foodjson);
			if (food==null) {
				response.put("success", false);
				response.put("message", "新增失敗");
			}else {
				response.put("succcess", true);
				response.put("message", "新增成功");
			}
		}
		return response.toString();
	}
	
	@PostMapping("/backstage/food/find")//postman test
	public String find(@RequestBody String json) {
		JSONObject responseJson = new JSONObject();
		JSONArray array = new JSONArray();
		List<Food> products = foodService.find(json);
		if (products!=null && !products.isEmpty()) {
			for(Food product : products) {
//				String make = DatetimeConverter.toString(product.getMake(), "yyyy-MM-dd");
//				String expire = DatetimeConverter.toString(product.getExpired(), "yyyy-MM-dd");
				String createDate = DatetimeConverter.toString(product.getCreateDate(), "yyyy-MM-dd");
				String modifyDate = DatetimeConverter.toString(product.getModifyDate(), "yyyy-MM-dd");

				JSONObject item = new JSONObject()
							.put("id", product.getId())
							.put("name", product.getName())
//							.put("name_eng", product.getName_eng())
							.put("price", product.getPrice())
							.put("count", product.getCount())
//							.put("description", product.getDescription())
//							.put("foodCategory_code", product.getFoodCategory_code())
//							.put("make", make)
//							.put("expired", expire)
							.put("createDate", createDate)
							.put("modifyDate", modifyDate);
				array.put(item);
			}
		}
		
		responseJson.put("list", array);
		
		long counts = foodService.count(json);
		responseJson.put("counts", counts);
		
		return responseJson.toString();
	}
	
	@GetMapping("/backstage/food/{pk}")//postman test
	public String findByFoodId(@PathVariable(name="pk") Integer id) {
		JSONObject jsonResponseObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Food food = foodService.findByFoodId(id);
		//有找到資料才會做if內程式碼，沒找到資料則會是一個名為list空陣列，toString()回傳String
		if (food!=null) {
			String createDate = DatetimeConverter.toString(food.getCreateDate(), "yyyy-MM-dd");
			String modifyDate = DatetimeConverter.toString(food.getModifyDate(), "yyyy-MM-dd");
			JSONObject item = new JSONObject();
			item.put("id", food.getId());
			item.put("name", food.getName());
//			item.put("name_eng", food.getName_eng());
			item.put("price", food.getPrice());
			item.put("count", food.getCount());
//			item.put("description", food.getDescription());
//			item.put("foodCategory_code", food.getFoodCategory_code());
//			item.put("make", food.getMake());
//			item.put("expired", food.getExpired());
			item.put("createDate", createDate);
			item.put("modifyDate", modifyDate);
			jsonArray.put(item);
		}
		
		jsonResponseObject.put("list", jsonArray);
		return jsonResponseObject.toString();
	}
	
	@PutMapping("/backstage/food/{pk}")//postman test
	public String modifyFood (@PathVariable(name="pk") Integer id ,@RequestBody String foodjson) {
		JSONObject responseJson = new JSONObject();
		if (id==null) {
			responseJson.put("success", false);
			responseJson.put("msg", "id為必要欄位");
		} else if (!foodService.existFoodById(id)) {
			responseJson.put("success", false);
			responseJson.put("msg", "id不存在");
		} else {
			Food food = foodService.modifyFood(foodjson);
			if (food == null) {
				responseJson.put("success", false);
				responseJson.put("msg", "修改失敗");
			} else {
				responseJson.put("success", true);
				responseJson.put("msg", "修改成功");
			}
		}
		
		
		return responseJson.toString();
	}
	
	@DeleteMapping("/backstage/food/{pk}")//postman test
	public String deleteFood (@PathVariable(name="pk") Integer id) {
		JSONObject responseJson = new JSONObject();
        if(id==null) {
            responseJson.put("success", false);
            responseJson.put("message", "id是必要欄位");
        } else if(!foodService.existFoodById(id)) {
            responseJson.put("success", false);
            responseJson.put("message", "id不存在");
        } else {
            if(foodService.deleteByFoodId(id)) {
                responseJson.put("success", true);
                responseJson.put("message", "刪除成功");
            } else {                
                responseJson.put("success", false);
                responseJson.put("message", "刪除失敗");
            }
        }
        return responseJson.toString();
	}
	
//	//picture
//	private byte[] picture = null;	
//	@PostConstruct
//	public void initialize() throws IOException {
//		byte[] buffer = new byte[8192];
//
//		ClassLoader classLoader = getClass().getClassLoader();
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		BufferedInputStream is = new BufferedInputStream(
//				classLoader.getResourceAsStream("static/no-image.jpg"));
//		int len = is.read(buffer);
//		while(len!=-1) {
//			os.write(buffer, 0, len);
//			len = is.read(buffer);
//		}
//		is.close();
//		this.picture = os.toByteArray();
//    }
//	
//	@GetMapping(
//			path = "/backstage/food/photo/{food_id}",
//			produces = {MediaType.IMAGE_JPEG_VALUE})
//	public @ResponseBody byte[] findPhotoById(@PathVariable(name="food_id") Integer id) {
//		Food foodPicture = foodService.findByFoodId(id);
//		byte[] result = this.picture;
//		if(foodPicture!=null) {
//			result =  foodPicture.getPicture();
//        }
//        return result;
//	}
	
}
