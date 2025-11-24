package com.example.shop.product.infrastructure;

import com.example.shop.product.domain.Product;
import com.example.shop.product.domain.ProductRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;


    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }

    @Override
    public void save(Product product) {

        productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID uuid) {
        return productJpaRepository.findById(uuid);
    }

    @Override
    public void deleteById(UUID uuid) {

        productJpaRepository.deleteById(uuid);
    }
}
