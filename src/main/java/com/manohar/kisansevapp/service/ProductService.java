package com.manohar.kisansevapp.service;

import java.util.List;
import java.util.Optional;

import com.manohar.kisansevapp.model.Product;

public interface ProductService {
	
	public void saveProduct(Product product);
	
	public Optional<Product> findByProductId(Long id);

	public boolean isProductExist(Product product);

	public List<Product> findAllProduct();

	public void updateProduct(Product product);

	public void deleteProductById(long id);

}
