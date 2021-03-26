package com.manohar.kisansevapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manohar.kisansevapp.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
