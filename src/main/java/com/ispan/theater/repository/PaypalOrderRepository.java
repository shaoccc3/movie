package com.ispan.theater.repository;

import com.ispan.theater.domain.PaypalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PaypalOrderRepository extends JpaRepository<PaypalOrder, Integer>, JpaSpecificationExecutor<PaypalOrder> {

}