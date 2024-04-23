package com.ispan.theater.repository;

import com.ispan.theater.domain.MovieAct;
import com.ispan.theater.domain.MovieActId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieActRepository extends JpaRepository<MovieAct, MovieActId> {
}