package com.example.shop.product.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Page<Product> findAll(Pageable pageable);


    void save(Product product);

    Optional<Product> findById(UUID uuid);

    void deleteById(UUID uuid);
}
