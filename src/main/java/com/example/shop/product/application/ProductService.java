package com.example.shop.product.application;

import com.example.shop.common.ResponseEntity;
import com.example.shop.product.application.dto.ProductCommand;
import com.example.shop.product.application.dto.ProductInfo;
import com.example.shop.product.domain.Product;
import com.example.shop.product.domain.ProductRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public ResponseEntity<List<ProductInfo>> findAll(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductInfo> products = page.stream()
            .map(ProductInfo::from)
            .toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), products, page.getTotalElements());
    }

    public ResponseEntity<ProductInfo> create(ProductCommand command) {

        UUID operator = command.operatorId() != null ? command.operatorId() : UUID.randomUUID();

        Product product = Product.create(command.name(), command.description(), command.price(),
            command.stock(), command.status(), operator);

        productRepository.save(product);

        return new ResponseEntity<>(HttpStatus.CREATED.value(), ProductInfo.from(product), 1);

    }

    public ResponseEntity<ProductInfo> update(String id, ProductCommand command) {

        // 상품 id
        UUID uuid = UUID.fromString(id);

        Product product = productRepository.findById(uuid).orElseThrow();
        UUID operator = command.operatorId() != null ? command.operatorId() : product.getModifyId();
        product.update(command.name(), command.description(), command.price(), command.stock(),
            command.status(), operator);

        productRepository.save(product);

        return new ResponseEntity<>(HttpStatus.OK.value(), ProductInfo.from(product), 1);

    }

    public ResponseEntity<Void> delete(String id) {

        UUID uuid = UUID.fromString(id);
        productRepository.deleteById(uuid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT.value(), null, 0);

    }
}
