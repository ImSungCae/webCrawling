package com.webcrawling.crawling.repository;

import com.webcrawling.crawling.entity.CrawlingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CrawlingRepository extends JpaRepository<CrawlingEntity, Long> {
}
