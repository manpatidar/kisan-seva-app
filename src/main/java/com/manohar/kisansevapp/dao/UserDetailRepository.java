package com.manohar.kisansevapp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.manohar.kisansevapp.model.UserDetails;

public interface UserDetailRepository extends JpaRepository<UserDetails, Long>{

	@Query(value="select * from user_details where id = :id", nativeQuery = true)
	Optional<UserDetails> findUserDetailById(@Param("id") Long id);
	
	@Modifying
	@Transactional
	@Query(value="update user_details set address = :address, city = :city, pincode = :pincode where id = :id", nativeQuery = true)
	void updateUserDetails(@Param("id") Long id, @Param("address") String address, @Param("city") String city, @Param("pincode") int pincode);

	
}
