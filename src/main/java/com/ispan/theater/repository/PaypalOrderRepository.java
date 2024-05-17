package com.ispan.theater.repository;

import com.ispan.theater.domain.PaypalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PaypalOrderRepository extends JpaRepository<PaypalOrder, Integer>, JpaSpecificationExecutor<PaypalOrder> {
  @Query("select p from PaypalOrder p where p.orderId = :orderId")
  public PaypalOrder findByOrderId(int orderId);
}