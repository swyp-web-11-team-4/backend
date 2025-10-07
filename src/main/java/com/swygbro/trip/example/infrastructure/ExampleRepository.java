package com.swygbro.trip.example.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swygbro.trip.example.domain.entity.Example;

public interface ExampleRepository extends JpaRepository<Example, Long> {
}
