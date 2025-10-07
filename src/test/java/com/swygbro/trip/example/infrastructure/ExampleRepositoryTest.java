package com.swygbro.trip.example.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.swygbro.trip.example.domain.entity.Example;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ExampleRepositoryTest {

    @Autowired
    private ExampleRepository exampleRepository;

    @Test
    @DisplayName("Example 엔티티 저장 및 조회 테스트")
    void saveAndFind() {
        // given
        String name = "test-example";
        Example newExample = new Example(null, name);

        // when
        Example savedExample = exampleRepository
            .saveAndFlush(newExample);
        Example foundExample = exampleRepository
            .findById(savedExample
                .getId())
            .orElse(null);

        // then
        assertThat(savedExample
            .getId())
            .isNotNull();
        assertThat(foundExample)
            .isNotNull();
        assertThat(foundExample
            .getId())
            .isEqualTo(savedExample
                .getId());
        assertThat(foundExample
            .getName())
            .isEqualTo(name);
    }
}
