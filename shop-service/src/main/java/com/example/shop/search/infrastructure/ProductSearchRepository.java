package com.example.shop.search.infrastructure;

import com.example.shop.search.domain.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument,String> {

}
