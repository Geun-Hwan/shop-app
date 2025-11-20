package com.example.shop.product.presentation.dto;

import com.example.shop.product.application.dto.ProductCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.util.StringUtils;

@Schema(description = "상품 등록/수정 요청 정보")
public record ProductRequest(
    @Schema(description = "상품명")
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    String status,
    String operatorId
) {

    public ProductCommand toCommand() {

        UUID operator = StringUtils.hasText(operatorId) ? UUID.fromString(operatorId) : null;
        return new ProductCommand(name, description, price, stock, status, operator);
    }

}
