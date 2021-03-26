package com.manohar.kisansevapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manohar.kisansevapp.model.Product;
import com.manohar.kisansevapp.model.User;
import com.manohar.kisansevapp.service.ProductService;
import com.manohar.kisansevapp.util.ResourceAlreadyExists;
import com.manohar.kisansevapp.util.ResourceNotFoundException;

@RequestMapping(path = "/api")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductController {
	
	private byte[] bytes;
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/product/get")
	public List<Product> getProducts() {
		return productService.findAllProduct();
	}
	
	@GetMapping("/product/get/{id}")
	public ResponseEntity<?> getProductById(@PathVariable("id") long id) {

		Optional<Product> product = productService.findByProductId(id);
		if (!product.isPresent()) {
			throw new ResourceNotFoundException("Product not found with id = " + id);
		}

		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	@PostMapping("/product/upload")
	public void uploadImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
		System.out.println("inside /product/upload method");
		this.bytes = file.getBytes();
	}
	
	@PostMapping("/product/add")
	public void createProduct(@RequestBody Product product) throws IOException {
		System.out.println("inside /product/add method");
		product.setPicByte(this.bytes);
		productService.saveProduct(product);
		this.bytes = null;
	}
	
	@PutMapping("/product/update/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
		Optional<Product> p = productService.findByProductId(id);
		Product prod = p.get();
		System.out.println("/product/update/{id}");
		if (!p.isPresent()) {
			throw new ResourceNotFoundException("Product not found with id = " + id);
		}
	
		prod.setName(product.getName());
		prod.setPrice(product.getPrice());
		prod.setDesc(product.getDesc());
		prod.setPicByte(this.bytes);
		System.out.println("prod" + prod.toString());
		productService.updateProduct(prod);
		this.bytes = null;
		return new ResponseEntity<>(prod, HttpStatus.OK);
	}
	
	@DeleteMapping("/product/delete/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {

		Optional<Product> product = productService.findByProductId(id);
		if (!product.isPresent()) {
			throw new ResourceNotFoundException("Product not found with id = " + id);
		}

		productService.deleteProductById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
