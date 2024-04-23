package com.ispan.theater.repository;

import com.ispan.theater.domain.MoviePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviePictureRepository extends JpaRepository<MoviePicture, Integer> {
}