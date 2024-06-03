package com.ispan.theater.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.ispan.theater.domain.FoodPicture;
import com.ispan.theater.repository.FoodPictureRepository;

@Service
@Transactional
public class FoodPictureService {
	@Autowired
	private FoodPictureRepository foodPictureRepository;
	
	
//	public FoodPicture saveFoodPicture(MuiltipartFile file,) {
//		
//	}
	public FoodPicture saveFoodPicture(FoodPicture fp) {
		return foodPictureRepository.save(fp);
	}
	
	public FoodPicture findByFoodId(Integer id) {
		if(id!=null) {
			Optional<FoodPicture> optional = foodPictureRepository.findById(id);
			if(optional.isPresent()) {
				return optional.get();				
			}
		}
		return null;
	}
	
	public List<FoodPicture> findAllPhotos(){
		return foodPictureRepository.findAll();
	}
}
