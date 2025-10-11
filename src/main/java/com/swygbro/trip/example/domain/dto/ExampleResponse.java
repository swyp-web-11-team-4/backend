package com.swygbro.trip.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "예제 응답 DTO")
public record ExampleResponse(
    @Schema(description = "응답 메시지", example = "Hello, World!") String message) {}
