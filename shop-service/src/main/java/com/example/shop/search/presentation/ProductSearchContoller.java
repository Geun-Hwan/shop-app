package com.example.shop.search.presentation;

import com.example.shop.search.application.SearchService;
import com.example.shop.search.application.dto.IndexStatusResponse;
import com.example.shop.search.application.dto.IndexUpdateResponse;
import com.example.shop.search.application.dto.ProductSearchResponse;
import com.example.shop.search.domain.ProductDocument;
import com.example.shop.search.presentation.dto.IndexConfigRequest;
import com.example.shop.search.presentation.dto.ProductIndexRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.v1}/search")
public class ProductSearchContoller {


    private  final SearchService searchService;

    @Operation(
        summary = "상품 검색",
        description = "키워드와 카테고리로 엘라스틱서치 상품 인덱스를 조회합니다."
    )
    @GetMapping("/products")
    public ProductSearchResponse searchProducts(
        @Parameter(description = "검색 키워드", example = "남자 신발")
        @RequestParam(required = false) String keyword,
        @Parameter(description = "카테고리 필터", example = "shoes")
        @RequestParam(required = false) String category,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        return searchService.searchProducts(keyword, category, pageable);
    }

    @Operation(
        summary = "상품 색인",
        description = "ES 상품 인덱스에 문서를 저장합니다. id와 시간은 서버에서 자동 생성됩니다."
    )
    @PostMapping("/products")
    public ResponseEntity<ProductDocument> indexProduct(
        @RequestBody
         ProductIndexRequest request
    ) {
        ProductDocument saved = searchService.indexProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
        summary = "상품 인덱스 설정/매핑 갱신",
        description = "인덱스가 없으면 생성하고, 있으면 레플리카/매핑을 업데이트합니다. 샤드 수 변경은 기존 인덱스에 적용되지 않습니다."
    )
    @PutMapping("/products/index")
    public IndexUpdateResponse updateIndex(
        @RequestBody
       IndexConfigRequest request
    ) {
        return searchService.applyProductIndexConfig(request);
    }

    @Operation(summary = "상품 인덱스 상태 조회", description = "인덱스 존재 여부, 설정, 매핑 정보를 반환합니다.")
    @GetMapping("/products/index")
    public IndexStatusResponse getIndexStatus() {
        return searchService.getProductIndexStatus();
    }

}
