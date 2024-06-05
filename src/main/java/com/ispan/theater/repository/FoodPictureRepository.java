package com.ispan.theater.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Food;
import com.ispan.theater.domain.FoodPicture;
@Repository
public interface FoodPictureRepository extends JpaRepository<FoodPicture, Integer> {

	@Query("select fp from FoodPicture fp where fp.food = :food")
	public List<FoodPicture> findFoodPictureByFoodId(@Param("food") Food food);
}
