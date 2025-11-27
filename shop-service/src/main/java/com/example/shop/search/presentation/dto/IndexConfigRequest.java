package com.example.shop.search.presentation.dto;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "인덱스 설정",example = "{\n  \"numberOfShards\": 3,\n  \"numberOfReplicas\": 0\n}")
public record IndexConfigRequest(Integer numberOfShards, Integer numberOfReplicas) {

}
