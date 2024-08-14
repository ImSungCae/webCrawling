package com.webcrawling.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SampleMapper {

    private final ObjectMapper objectMapper;

    public SampleMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Sample fromEntity(SampleEntity entity) {
        return objectMapper.convertValue(entity, Sample.class);
    }

    public SampleEntity toEntity(Sample dto) {
        return objectMapper.convertValue(dto, SampleEntity.class);
    }

    public List<Sample> fromEntities(List<SampleEntity> entities) {
        return entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SampleEntity> toEntities(List<Sample> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}