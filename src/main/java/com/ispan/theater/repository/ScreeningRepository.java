package com.ispan.theater.repository;

import com.ispan.theater.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
}