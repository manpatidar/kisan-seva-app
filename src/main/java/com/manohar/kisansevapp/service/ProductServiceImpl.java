package com.manohar.kisansevapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.manohar.kisansevapp.dao.ProductRepository;
import com.manohar.kisansevapp.model.Product;

@Service
@CacheConfig(cacheNames={"products"})
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public void saveProduct(Product product) {
		productRepository.save(product);
	}

	@Override
	@Cacheable(cacheNames="ProductCache", key="#id")
	public Optional<Product> findByProductId(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public boolean isProductExist(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Cacheable
	public List<Product> findAllProduct() {
		return productRepository.findAll();
	}

	@Override
	public void updateProduct(Product product) {
		saveProduct(product);
	}

	@Override
	public void deleteProductById(long id) {
		productRepository.deleteById(id);
	}

}
