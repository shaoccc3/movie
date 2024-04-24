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
    @Query("select c from Movie c where c.name like concat('%', :name ,'%')")
    public List<Movie> fineMovieByNameLike(@Param("name") String name);
    @Query("select count(*) from Movie c where c.name like concat('%', :name ,'%')")
    public long countByNameLike(String name);
}