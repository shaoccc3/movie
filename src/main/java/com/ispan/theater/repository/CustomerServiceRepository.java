package com.ispan.theater.repository;

import com.ispan.theater.domain.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Integer> {
}