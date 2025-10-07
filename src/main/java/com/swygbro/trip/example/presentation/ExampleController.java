package com.swygbro.trip.example.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swygbro.trip.example.application.ExampleUseCase;
import com.swygbro.trip.example.domain.dto.ExampleResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Example",
    description = "API 예제")
@RestController
@RequestMapping("/api/v1/example")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleUseCase exampleUseCase;

    @Operation(
        summary = "Hello World",
        description = "간단한 환영 메시지를 반환합니다.")
    @GetMapping("/hello")
    public ResponseEntity<ExampleResponse> hello() {
        return ResponseEntity
            .ok(exampleUseCase
                .getHelloMessage());
    }
}
