package com.ispan.theater.repository;

import com.ispan.theater.domain.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
}