package com.ispan.theater.repository;

import com.ispan.theater.domain.MoviePicture;
import com.ispan.theater.dto.MoviePicDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviePictureRepository extends JpaRepository<MoviePicture, Integer> {
    @Query(value = "select c.* from Movie_Picture as c where c.movie_id = :movieId",nativeQuery = true)
    List<MoviePicDto> findByMovieId(@Param("movieId") Integer movieId);
}