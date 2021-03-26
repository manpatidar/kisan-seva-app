package com.manohar.kisansevapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manohar.kisansevapp.dao.CartRepository;
import com.manohar.kisansevapp.dao.ProductRepository;
import com.manohar.kisansevapp.model.CartDetails;
import com.manohar.kisansevapp.model.Product;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public void saveCart(CartDetails cart) {
		String userName = cart.getUserName();
		CartDetails cartItem = findByProductId(userName, cart.getProductId());
		if (cartItem.getQty() > 0) {
			cartItem.setQty(cartItem.getQty() + 1);
			cartItem.setActualPrice(cartItem.getActualPrice() + cart.getActualPrice());
			cartItem.setOfferedPrice(cartItem.getOfferedPrice() + cart.getOfferedPrice());
			cartRepository.save(cartItem);
		} else {
			System.out.println(cart.getProductName() + cart.getUserName() + cart.getProductId());
			cartRepository.save(cart);
		}

	}

	@Override
	public CartDetails findByProductId(String userName, Long id) {
		CartDetails cartItem = new CartDetails();
		try {
			Optional<CartDetails> item = cartRepository.findByProductId(userName, id);

			if (item.isPresent()) {
				cartItem = item.get();
				return cartItem;
			}
		} catch (Exception e) {
		} finally {
			System.out.println("Inside findBYProductId " + cartItem.getProductName() + cartItem.getUserName() + cartItem.getProductId());
			return cartItem;
		}
	}

	@Override
	public boolean isProductExist(CartDetails cart) {
		return false;
	}

	@Override
	public List<CartDetails> findAllProduct() {
		return cartRepository.findAll();
	}

	@Override
	public boolean updateCartQuantity(Long id, String userName, String action) {

		CartDetails cart = findByProductId(userName, id);
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {

			Product p = product.get();
			double ap = p.getPrice();

			int quantity = 0;
			double price = 0;
			double offPrice = 0.0;
			if (action.equals("plus")) {
				quantity = cart.getQty() + 1;
				price = ap * quantity;
				offPrice = (price) - (price*15/100);
			}
			if (action.equals("minus")) {
				quantity = cart.getQty() - 1;
				price = ap * quantity;
				offPrice = (price) - (price*15/100);
			}
			cartRepository.updateProductByProductId(userName, quantity, price, offPrice, id);
			return true;
		}
		return false;
	}

	@Override
	public List<CartDetails> findProductByUserName(String username) {
		return cartRepository.findProductByUserName(username);
	}

	@Override
	public void deleteProductById(String userName, long id) {
		cartRepository.deleteProductByProductId(userName, id);
	}

}
