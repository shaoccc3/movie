package com.ispan.theater.repository;

import com.ispan.theater.dao.MovieDao;
import com.ispan.theater.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> ,MovieDao {
    @Query("select c from Movie c where c.name = :name")
    public Movie findByName(@Param("name") String name);

}