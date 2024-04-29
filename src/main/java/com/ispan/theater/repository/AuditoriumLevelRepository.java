package com.ispan.theater.repository;

import com.ispan.theater.domain.AuditoriumLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriumLevelRepository extends JpaRepository<AuditoriumLevel, Integer> {
}