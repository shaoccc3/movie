package com.ispan.theater.repository;

import com.ispan.theater.dao.UserDao;
import com.ispan.theater.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> ,UserDao{
	
	@Query("from User where email = :email")
	public User findByEmail(@Param("email") String email);
	
	@Query("from User where phone = :phone")
	public User findByPhone(@Param("phone")String phone);
	
	@Query("from User where phone = :phone or email = :email")
	public User findByEmailOrPhone(@Param("email") String email,@Param("phone")String phone);
>>>>>>> dac7724988aae227e00e35ee36db247e620f8fc8
}