package com.ispan.theater.repository;

import com.ispan.theater.domain.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query(value="select o from Order o where o.user.id=:id")
	Optional<Order> findOrderByUserId(@Param("id")Integer id);
}