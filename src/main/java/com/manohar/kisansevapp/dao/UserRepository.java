package com.manohar.kisansevapp.dao;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.manohar.kisansevapp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value="select * from user where user_name = :userName", nativeQuery = true)
	Optional<User> findByUsername(@Param("userName") String userName);

	@Query(value="select * from user where user_name = :userName or mobile = :mobile", nativeQuery = true)
	Optional<User> findUserByMobileUserName(@Param("userName") String userName, @Param("mobile") Long mobile);

	@Query(value="select * from user where mobile = :mobile", nativeQuery = true)
	Optional<User> findByMobile(@Param("mobile") Long mobile);
	
}
