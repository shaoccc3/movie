package com.ispan.theater.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
	@Query(value="select c from Cinema as c where c.name=:name")
	Optional<Cinema> findCinemaByName(@Param(value="name")String name);
	
	@Query(value="select c.name from Cinema as c ")
	List<String> findAllCinemaName();
}