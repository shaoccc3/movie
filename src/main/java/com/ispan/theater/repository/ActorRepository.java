package com.ispan.theater.repository;

import com.ispan.theater.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    @Query("select c from Actor  c where c.name like %:name%")
    public List<Actor> findByName(@Param("name") String name);
}