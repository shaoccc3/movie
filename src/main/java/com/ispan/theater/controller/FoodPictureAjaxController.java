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
	
	@PostMapping("/food/photos/upload")
	public String uploadPost(@RequestParam Integer id,
			@RequestParam MultipartFile picture,
			Model model)throws Exception {
		
		FoodPicture newFp = new FoodPicture();
		newFp.setId(id);
		newFp.setPicture(picture.getBytes());
		
		foodPictureService.saveFoodPicture(newFp);
		
		model.addAttribute("okMsg","上傳成功!");
		
		
		return "photos/uploadPage";
	}
	
	@GetMapping(
			path = "/food/photo/{food_pictureid}",
			produces = {MediaType.IMAGE_JPEG_VALUE}
			)
	public @ResponseBody byte[] findPhotoByPhotoId(@PathVariable(name="food_pictureid") Integer food_pictureid) {
		FoodPicture foodPicture = foodPictureService.findByFoodId(food_pictureid);
		byte[] result = this.picture;
		if(foodPicture!=null) {
			result = foodPicture.getPicture();
        }
        return result;
	}
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
	
}
