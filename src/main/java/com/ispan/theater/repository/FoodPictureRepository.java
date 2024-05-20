package com.ispan.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.FoodPicture;
@Repository
public interface FoodPictureRepository extends JpaRepository<FoodPicture, Integer> {

}
