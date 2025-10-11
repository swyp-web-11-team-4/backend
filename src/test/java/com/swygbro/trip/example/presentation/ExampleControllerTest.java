package com.swygbro.trip.example.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.swygbro.trip.example.application.ExampleUseCase;
import com.swygbro.trip.example.domain.dto.ExampleResponse;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExampleController.class)
@ActiveProfiles("test")
class ExampleControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ExampleUseCase exampleUseCase;

  @Test
  @DisplayName("Hello API 테스트")
  void hello() throws Exception {
    // given
    String expectedMessage = "Hello, Test!";
    given(exampleUseCase.getHelloMessage()).willReturn(new ExampleResponse(expectedMessage));

    // when & then
    mockMvc
        .perform(get("/api/v1/example/hello"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(expectedMessage));
  }
}
