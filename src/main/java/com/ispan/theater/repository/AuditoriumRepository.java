package com.ispan.theater.repository;

import com.ispan.theater.domain.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {
}