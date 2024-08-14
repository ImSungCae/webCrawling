package com.webcrawling.crawling;

import com.webcrawling.crawling.service.CrawlingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class CrawlingServiceTest {
    @Autowired
    private CrawlingService crawlingService;

    @Test
    public void testCrawling(){
        crawlingService.crawling();
    }

    @Test
    public void testDeleteCrawling(){
        crawlingService.deleteCrawling();
    }



}
