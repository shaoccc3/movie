package com.ispan.theater.repository;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
    @Query("select c from Screening c where c.movie = :movie")
    List<Screening> findByMovie(@Param("movie") Movie movie);
}