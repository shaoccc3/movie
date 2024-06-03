package com.ispan.theater.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.theater.domain.FoodPicture;
import com.ispan.theater.service.FoodPictureService;

import jakarta.annotation.PostConstruct;

@RestController
public class FoodPictureAjaxController {
	@Autowired
	private FoodPictureService foodPictureService;
	
	@PostMapping("/backstage/food/uploadPhoto")
	public FoodPicture uploadPost(@RequestParam Integer id,
			@RequestParam MultipartFile picture,
			Model model)throws Exception {
		
		FoodPicture newFp = new FoodPicture();
		newFp.setId(id);
		newFp.setPicture(picture.getBytes());
		
		foodPictureService.saveFoodPicture(newFp);
		
//		model.addAttribute("okMsg","上傳成功!");
		
		
		return newFp;
	}
	
//	@PostMapping("/backstage/food/uploadPhoto/{id}")
//	public ResponseEntity<String> uploadFoodImage(@RequestParam("picture") MultipartFile picture, @PathVariable(name="id") Integer id
//			) {
//		FoodPicture newFp = foodPictureService.findByFoodId(id);
//		try {
//			
//			if (picture.isEmpty()) {
//				return ResponseEntity.badRequest().body("空檔案");
//			}
//			byte[] image = picture.getBytes();
//			newFp.setPicture(image);
//			foodPictureService.saveFoodPicture(newFp);
//			
//			return ResponseEntity.ok("上傳成功");
//		} catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上傳失敗: " + e.getMessage());
//
//		}
//		
//	}
	
	private byte[] picture = null;	
	@PostConstruct
	public void initialize() throws IOException {
		byte[] buffer = new byte[8192];

		ClassLoader classLoader = getClass().getClassLoader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedInputStream is = new BufferedInputStream(
				classLoader.getResourceAsStream("static/no-image.jpg"));
		int len = is.read(buffer);
		while(len!=-1) {
			os.write(buffer, 0, len);
			len = is.read(buffer);
		}
		is.close();
		this.picture = os.toByteArray();
    }
	
	@GetMapping(
			path = "/backstage/food/photo/{food_pictureid}",
			produces = {MediaType.IMAGE_JPEG_VALUE})
	public @ResponseBody byte[] findPhotoByPhotoId(@PathVariable(name="food_pictureid") Integer id) {
		FoodPicture foodPicture = foodPictureService.findByFoodId(id);
		byte[] result = this.picture;
		if(foodPicture!=null) {
			result =  foodPicture.getPicture();
        }
        return result;
	}
	
}
