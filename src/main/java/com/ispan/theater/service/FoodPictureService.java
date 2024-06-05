package com.ispan.theater.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.theater.domain.Food;
import com.ispan.theater.domain.FoodPicture;
import com.ispan.theater.repository.FoodPictureRepository;
import com.ispan.theater.repository.FoodRepository;

@Service
@Transactional
public class FoodPictureService {
	@Autowired
	private FoodPictureRepository foodPictureRepository;
	@Autowired
	private FoodRepository foodRepository;
	
	public List<FoodPicture> getFoodPicture(Integer fid) {
		Food food = foodRepository.findById(fid).orElse(null);
		return foodPictureRepository.findFoodPictureByFoodId(food);
	}
	
	public boolean insertFoodPicture(List<MultipartFile> files, Integer foodId) throws IOException {
		Food food = foodRepository.findById(foodId).orElse(null);

        if( food !=null){
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                FoodPicture foodPicture = new FoodPicture();
                foodPicture.setFood(food);
                foodPicture.setPicture(bytes);
                foodPicture.setFilename(fileName);
                foodPictureRepository.save(foodPicture);
            }
            return true;
        }
        return false;
    }

	//find picture
    public FoodPicture findFoodPictureById(Integer id) {
		if(id!=null) {
			Optional<FoodPicture> optional = foodPictureRepository.findById(id);
			if(optional.isPresent()) {
				return optional.get();				
			}
		}
		return null;
	}
    
    public boolean delete(Integer id) {
		if(id!=null) {
			Optional<FoodPicture> optional = foodPictureRepository.findById(id);
			if(optional.isPresent()) {
				foodPictureRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}
    public boolean existById(Integer id) {
		if(id!=null) {
			return foodPictureRepository.existsById(id);
		}
		return false;
	}
//	//upload picture
//	public Optional<FoodPicture> findFoodPictureByFoodId(Integer foodId) {
//		System.out.println("foodId:"+foodId);
//		return foodPictureRepository.findFoodPictureByFoodId(foodId);
//	}
//	
//	public List<FoodPicture> findAllPhotos(){
//		return foodPictureRepository.findAll();
//	}
}
