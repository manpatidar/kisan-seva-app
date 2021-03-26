package com.manohar.kisansevapp.service;

import java.util.List;
import java.util.Optional;

import com.manohar.kisansevapp.model.CartDetails;

public interface CartService {

	public void saveCart(CartDetails cart);
	
	public CartDetails findByProductId(String userName, Long id);

	public boolean isProductExist(CartDetails cart);

	public List<CartDetails> findAllProduct();

	public void deleteProductById(String userName, long id);

	public List<CartDetails> findProductByUserName(String username);

	public boolean updateCartQuantity(Long id, String userName, String action);
}
