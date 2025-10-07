package com.swygbro.trip.example.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.swygbro.trip.example.domain.dto.ExampleResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ExampleServiceTest {

    @InjectMocks
    private ExampleService exampleService;

    @Test
    @DisplayName("Hello 메시지 생성 테스트")
    void getHelloMessage() {
        // given
        String expectedMessage = "Hello, World!";

        // when
        ExampleResponse response = exampleService
            .getHelloMessage();

        // then
        assertThat(response)
            .isNotNull();
        assertThat(response
            .message())
            .isEqualTo(expectedMessage);
    }
}
