package com.example.shop.product.infrastructure;

import com.example.shop.product.domain.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, UUID> {

}
