package com.webcrawling.crawling.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcrawling.crawling.dto.CrawlingDTO;
import com.webcrawling.crawling.entity.CrawlingEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CrawlingMapper {
    private final ObjectMapper objectMapper;

    public CrawlingMapper(ObjectMapper objectMapper) { this.objectMapper = objectMapper; }

    public CrawlingDTO fromEntity(CrawlingEntity entity) { return objectMapper.convertValue(entity, CrawlingDTO.class); }

    public CrawlingEntity toEntity(CrawlingDTO dto) { return objectMapper.convertValue(dto, CrawlingEntity.class );}

    public List<CrawlingDTO> fromEntities(List<CrawlingEntity> entities) {
        return entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CrawlingEntity> toEntities(List<CrawlingDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
