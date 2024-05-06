package com.ispan.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.theater.dao.UserDao;
import com.ispan.theater.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> ,UserDao{
	
	@Query("from User where email = :email")
	public User findByEmail(@Param("email") String email);
	
	@Query("from User where phone = :phone")
	public User findByPhone(@Param("phone")String phone);
	
	@Query("from User where phone = :phone or email = :email")
	public User findByEmailOrPhone(@Param("email") String email,@Param("phone")String phone);

}