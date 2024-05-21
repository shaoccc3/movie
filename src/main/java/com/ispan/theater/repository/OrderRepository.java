package com.ispan.theater.repository;

import com.ispan.theater.domain.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	//分頁  select * FROM Seat order by seat_id desc offset 0 rows fetch next 10 rows only 
	
	@Query(value="select o from Order o where o.user.id=:id")
	Optional<Order> findOrderByUserId(@Param("id")Integer id);
	
	@Modifying
	@Query(value="insert into \"Order\"(create_date,modify_date,order_amount,movie_id,user_id,payment_condition) values(:createDate,:modifyDate,:orderAmount,:movieId,:userId,:condition)",nativeQuery=true)
	Integer createOrder(@Param("createDate")String createDate,@Param("modifyDate")String modifyDate,@Param("orderAmount")Double orderAmount,@Param("movieId")Integer movieId,@Param("userId")Integer userId,@Param("condition")Integer condition);
	
	@Query(value="select top(1) o.* from \"Order\" as o where create_date like :createDate% and user_id=:id order by create_date desc",nativeQuery = true)
	Optional<Order> findOrderByUserIdAndCreateDate(@Param("createDate")String createDate,@Param("id")Integer id);
	//distinct
	
	//select ROW_NUMBER() over(order by Seat.seat_id asc) as 'no',od.order_id,c.location_category+c.name as 'location',m.name as movie_name,substring(CONVERT(varchar,s.Start_time),1,19) as Start_time,CONVERT(varchar, a.auditorium_number)+'廳'+CONVERT(varchar, Seat.seat_row)+'排'+CONVERT(varchar, seat_column)+'位' as seat,substring(CONVERT(varchar,o.create_date),1,19) as create_date from OrderDetail as od join Ticket as t on od.ticket_id=t.Ticket_id join Screening as s on t.Screening_id=s.Screening_id join movie as m on m.movie_id=t.movie_id  join Seat on Seat.seat_id=t.seat_id join auditorium as a on a.auditorium_id=s.auditorium_id join cinema as c on a.cinema_id=c.cinema_id join \"Order\" as o on o.order_id=od.order_id where od.order_id= :orderId
	@Query(value="select ROW_NUMBER() over(order by Seat.seat_id asc) as 'no',od.order_id,c.location_category+c.name as 'location',m.name as movie_name,substring(CONVERT(varchar,s.Start_time),1,19) as Start_time,CONVERT(varchar, a.auditorium_number)+'廳'+CONVERT(varchar, Seat.seat_row)+'排'+CONVERT(varchar, seat_column)+'位' as seat,substring(CONVERT(varchar,o.create_date),1,19) as create_date from OrderDetail as od join Ticket as t on od.ticket_id=t.Ticket_id join Screening as s on t.Screening_id=s.Screening_id  join Seat on Seat.seat_id=t.seat_id join auditorium as a on a.auditorium_id=s.auditorium_id join cinema as c on a.cinema_id=c.cinema_id join \"Order\" as o on o.order_id=od.order_id join movie as m on m.movie_id=o.movie_id  where od.order_id= :orderId",nativeQuery=true)
	List<Map<String,String>> orderCompleted(@Param("orderId")Integer orderId);
	
	@Query(value="select ROW_NUMBER() over(order by Seat.seat_id asc) as 'no',od.order_id,c.location_category+c.name as 'location',m.name as movie_name,substring(CONVERT(varchar,s.Start_time),1,19) as Start_time,CONVERT(varchar, a.auditorium_number)+'廳'+CONVERT(varchar, Seat.seat_row)+'排'+CONVERT(varchar, seat_column)+'位' as seat,substring(CONVERT(varchar,o.create_date),1,19) as create_date from OrderDetail as od join Ticket as t on od.ticket_id=t.Ticket_id join Screening as s on t.Screening_id=s.Screening_id  join Seat on Seat.seat_id=t.seat_id join auditorium as a on a.auditorium_id=s.auditorium_id join cinema as c on a.cinema_id=c.cinema_id join \"Order\" as o on o.order_id=od.order_id join movie as m on m.movie_id=o.movie_id  where o.payment_no= :paymentNo",nativeQuery=true)
	List<Map<String,String>> orderCompletedByECPay(@Param("paymentNo")String paymentNo);
	
	//select od.order_id,count(*) as \"count\" from OrderDetail as od join \"Order\" as o on od.order_id=o.order_id where o.user_id= :userId group by od.order_id
	@Query(value="select od.order_id,count(*) as ticketCount from OrderDetail as od join \"Order\" as o on od.order_id=o.order_id where o.user_id= :userId group by od.order_id",nativeQuery=true)
	List<Map<String,String>> orderDetailCountByUserId(@Param("userId")Integer userId);
	
	@Query(value="select od.order_id,count(*) as \"count\" from OrderDetail as od join \"Order\" as o on od.order_id=o.order_id where o.order_id= :orderId group by od.order_id",nativeQuery=true)
	List<Map<String,Integer>> orderDetailCountByOrderId(@Param("orderId")Integer orderId);
	
	@Modifying
	@Query(value="update \"Order\" set payment_condition=1 where order_id= :orderId",nativeQuery=true)
	void setOrderConditionByUserId(@Param("orderId")Integer orderId);
	
	@Modifying
	@Transactional
	@Query(value="update \"Order\" set payment_condition=1 where payment_no= :paymentNo",nativeQuery=true)
	void setOrderConditionByPaymentNo(@Param("paymentNo")String paymentNo);
	
	@Modifying
	@Transactional
	@Query(value="update \"Order\" set payment_no= :paymentNo where order_id= :orderId",nativeQuery=true)
	void setPaymentNoByOrderId(@Param("paymentNo")String paymentNo,@Param("orderId")Integer orderId);
	
	@Modifying
	@Transactional
	@Query(value="update \"Order\" set payment_no=:paymentNo,payment_condition=1 where order_id= :orderId",nativeQuery=true)
	void setPaymentNoAndConditionByOrderId(@Param("paymentNo")String paymentNo,@Param("orderId")Integer orderId);
	
	@Query(value="select ROW_NUMBER() over(order by o.create_date desc) as 'no',o.order_id,m.name,SUBSTRING(convert(varchar(19),o.create_date),1,19) as create_date,o.order_amount,o.supplier FROM \"Order\" as o join movie as m on o.movie_id=m.movie_id where o.user_id=:userId order by o.create_date desc offset :page rows fetch next 10 rows only ",nativeQuery=true)
	List<Map<String,String>> getOrderByUser(@Param("userId")Integer userId,@Param("page")Integer page);
	
	@Query(value="select count(order_id) as order_total from \"Order\" where user_id=:userId",nativeQuery=true)
	Map<String,Integer> orderTotalByUserId(@Param("userId")Integer userId);
	
}