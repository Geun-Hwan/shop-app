package com.example.shop.search.application;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import com.example.shop.search.application.dto.IndexStatusResponse;
import com.example.shop.search.application.dto.IndexUpdateResponse;
import com.example.shop.search.application.dto.ProductSearchResponse;
import com.example.shop.search.domain.ProductDocument;
import com.example.shop.search.infrastructure.ProductSearchRepository;
import com.example.shop.search.presentation.dto.IndexConfigRequest;
import com.example.shop.search.presentation.dto.ProductIndexRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductSearchRepository productSearchRepository;
    private  final ElasticsearchOperations operations;

    public ProductSearchResponse searchProducts(String keyword, String category,
        Pageable pageable) {
        NativeQuery query = NativeQuery.builder().withQuery(q->q.bool(b->{
            if (StringUtils.hasText(keyword)) {
                b.must(m->m.match(mm->mm.field("name").query(keyword).operator(Operator.And))); // "남자" AND "신발"
            }
            if(StringUtils.hasText(category)){
                b.filter(f->f.term(t->t.field("category").value(category)));
            }
            return  b;
        })).withPageable(pageable).build();

        SearchHits<ProductDocument> hits =operations.search(query, ProductDocument.class);
        List<ProductDocument> items =hits.getSearchHits().stream().map(SearchHit::getContent).toList();
        return new ProductSearchResponse(hits.getTotalHits(),items);
    }

    public ProductDocument indexProduct(ProductIndexRequest request) {

        Instant updateTime = Instant.now();
        ProductDocument doc = new ProductDocument(null, request.name(), request.brand(),
            request.category(), request.price(), updateTime);

        return productSearchRepository.save(doc);

    }

    public IndexUpdateResponse applyProductIndexConfig(IndexConfigRequest request) {
        IndexOperations indexOperations = operations.indexOps(ProductDocument.class);
        boolean created =false;
        boolean settingsUpdated = false;
        boolean mappingUpdated = false;

        if(!indexOperations.exists()){
            // 새 인덱스 생성 시 샤드 + replica 설정 가능
            Document settings = Document.create();
            if(request.numberOfShards() != null){
                settings.put("index.number_of_shards", request.numberOfShards());
            }
            if(request.numberOfReplicas() != null){
                settings.put("index.number_of_replicas", request.numberOfReplicas());
            }
            created = indexOperations.create(settings);
        } else {
            // 기존 인덱스가 존재하면 래플리카/매핑만 수정 가능

        }

        mappingUpdated = indexOperations.putMapping(indexOperations.createMapping(ProductDocument.class));
        return new IndexUpdateResponse(created,settingsUpdated,mappingUpdated);
    }

    public IndexStatusResponse getProductIndexStatus() {
        IndexOperations indexOperations = operations.indexOps(ProductDocument.class);
        boolean exists = indexOperations.exists();
        Map<String, Object> settings = exists? new HashMap<>(indexOperations.getSettings()):Map.of();
        Map<String, Object> mapping = exists? new HashMap<>(indexOperations.getMapping()):Map.of();
        return new IndexStatusResponse(settings,mapping);
    }
}
