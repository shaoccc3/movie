package com.ispan.theater.repository;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {
    @Query("select c from Auditorium c where c.cinema = :cinema")
    public List<Auditorium> findByCinema(@Param("cinema") Cinema cinema);
}