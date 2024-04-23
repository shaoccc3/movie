package com.ispan.theater.repository;

import com.ispan.theater.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    @Query("select c from Actor  c where c.name like %:name%")
    public List<Actor> findByName(@Param("name") String name);
}