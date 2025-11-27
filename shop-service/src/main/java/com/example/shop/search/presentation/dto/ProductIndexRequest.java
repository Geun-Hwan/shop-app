package com.example.shop.search.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "상품 색인 요청",
    example = """
        {
          "name": "남자 셔츠",
          "brand": "SHOP",
          "category": "shirts",
          "price": 59000
        }"""
)
public record ProductIndexRequest(String name , String brand , String category, Integer price) {

}
