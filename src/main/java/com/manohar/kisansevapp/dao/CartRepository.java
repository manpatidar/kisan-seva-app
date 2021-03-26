package com.manohar.kisansevapp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.manohar.kisansevapp.model.CartDetails;

@Repository
public interface CartRepository extends JpaRepository<CartDetails,Long>{

	@Query(value="select * from cart_details where product_id = :productId and user_name = :userName", nativeQuery = true)
	Optional<CartDetails> findByProductId(@Param("userName") String userName, @Param("productId") Long productId);

	@Query(value="select * from cart_details where user_name = :username", nativeQuery = true)
	List<CartDetails> findProductByUserName(@Param("username") String username);
	
	@Modifying
	@Transactional
	@Query(value="delete from cart_details where product_id = :productId and user_name = :userName", nativeQuery = true)
	void deleteProductByProductId(@Param("userName") String userName, @Param("productId") Long productId);
	
	@Modifying
	@Transactional
	@Query(value="update cart_details set qty = :quantity, actual_price = :price, offered_price = :offPrice where product_id = :productId and user_name = :userName", nativeQuery = true)
	void updateProductByProductId(@Param("userName") String userName,
								  @Param("quantity") int quantity,
								  @Param("price") double price, 
								  @Param("offPrice") double offPrice, 
								  @Param("productId") long productId);

}
