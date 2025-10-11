package com.swygbro.trip.example.application;

import org.springframework.stereotype.Service;

import com.swygbro.trip.example.domain.dto.ExampleResponse;

@Service
public class ExampleService implements ExampleUseCase {
  @Override
  public ExampleResponse getHelloMessage() {
    return new ExampleResponse("Hello, World!");
  }
}
