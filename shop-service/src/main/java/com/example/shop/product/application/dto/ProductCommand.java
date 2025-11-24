package com.example.shop.product.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductCommand(
    UUID sellerId,

    String name,
    String description,
    BigDecimal price,
    Integer stock,
    String status,
    UUID operatorId) {

}
