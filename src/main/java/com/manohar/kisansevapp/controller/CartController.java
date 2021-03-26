package com.manohar.kisansevapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manohar.kisansevapp.model.CartDetails;
import com.manohar.kisansevapp.service.CartService;
import com.manohar.kisansevapp.util.ResourceNotFoundException;

@RequestMapping(path = "/api")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CartController {

	@Autowired
	CartService cartService;

	@GetMapping("/cart/get")
	public List<CartDetails> getProducts() {
		return cartService.findAllProduct();
	}

	@GetMapping("/cart/get/{userName}")
	public List<CartDetails> getProductById(@PathVariable("userName") String username) {
		return cartService.findProductByUserName(username);
	}

	@PostMapping("/cart/add")
	public ResponseEntity<?> createProduct(@RequestBody CartDetails product) throws IOException {
		System.out.println("inside /cart/add method");
		System.out.println("product " + product.getProductName());
		cartService.saveCart(product);
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@PutMapping("/cart/update/{userName}/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("userName") String userName, @PathVariable("id") long id, @RequestBody String action) {
		CartDetails p = cartService.findByProductId(userName, id);
		if (p.getId() <= 0) {
			throw new ResourceNotFoundException("Product not found with id = " + id);
		}
		System.out.println("/product/update/{id}");

		if (cartService.updateCartQuantity(id, userName, action)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
	}

	@DeleteMapping("/cart/delete/{userName}/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("userName") String userName, @PathVariable("id") long id) {

		CartDetails product = cartService.findByProductId(userName, id);
		if (product.getId() <= 0) {
			throw new ResourceNotFoundException("Product not found with id = " + id);
		}

		cartService.deleteProductById(userName, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
