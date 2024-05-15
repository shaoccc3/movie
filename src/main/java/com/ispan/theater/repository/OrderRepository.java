package com.ispan.theater.repository;

import com.ispan.theater.domain.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	@Query(value="select o from Order o where o.user.id=:id")
	Optional<Order> findOrderByUserId(@Param("id")Integer id);
	
	@Modifying
	@Query(value="insert into \"Order\"(create_date,modify_date,order_amount,movie_id,user_id) values(:createDate,:modifyDate,:orderAmount,:movieId,:userId)",nativeQuery=true)
	Integer createOrder(@Param("createDate")String createDate,@Param("modifyDate")String modifyDate,@Param("orderAmount")Double orderAmount,@Param("movieId")Integer movieId,@Param("userId")Integer userId);
	
	@Query(value="select top(1) o.* from \"Order\" as o where create_date like :createDate% and user_id=:id order by create_date desc",nativeQuery = true)
	Optional<Order> findOrderByUserIdAndCreateDate(@Param("createDate")String createDate,@Param("id")Integer id);
}