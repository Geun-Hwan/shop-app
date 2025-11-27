package com.example.shop.search.application.dto;

import com.example.shop.search.domain.ProductDocument;
import java.util.List;

public record ProductSearchResponse(long count, List<ProductDocument> items) {

}
