package com.webcrawling.crawling.service;

import com.webcrawling.crawling.dto.CrawlingDTO;
import com.webcrawling.crawling.mapper.CrawlingMapper;
import com.webcrawling.crawling.repository.CrawlingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlingService {

    private final WebDriver webDriver;
    private final CrawlingRepository crawlingRepository;
    private final CrawlingMapper crawlingMapper;

    @Value("${crawling.url}")
    private String url;

    public void crawling() {
        String originalTab = webDriver.getWindowHandle();
        try {
            log.info("페이지 접속");
            webDriver.get(url);

            log.info("페이지 로딩 대기(최대 10초)");
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("/html/body/main/section/div[7]/div/section/div[2]/div[2]/table/tbody/tr")
            ));

            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            List<CrawlingDTO> dtos = new ArrayList<>();

            WebElement element = elements.get(0);

//            for (WebElement element : elements) {
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                    js.executeScript("arguments[0].click();", element.findElement(By.cssSelector("th > a")));

                    Set<String> allTabs = webDriver.getWindowHandles();
                    for (String tab : allTabs) {
                        if (!tab.equals(originalTab)) {
                            webDriver.switchTo().window(tab);
                            break;
                        }
                    }

                    wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

                    String bootCampImageUrl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/section/header/div[2]/div/img"))).getAttribute("src");

                    WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("/html/body/main/section/section/div[1]")
                    ));

                    String educationalName = item.findElement(By.xpath("div[1]/div[2]")).getText();
                    String curriculumName = item.findElement(By.xpath("div[1]/div[3]")).getText();
                    List<String> categoryList = new ArrayList<>();
                    List<WebElement> categoryWeb = item.findElements(By.xpath("div[2]/ul/li[1]/ul/li[1]/div/div[2]/div[4]/ul/li"));
                    for (WebElement category : categoryWeb) {
                        categoryList.add(category.findElement(By.xpath("span")).getText());
                    }
                    String recruitmentEndDate = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[3]/div/ul/li[1]/div/ul/li[2]/div/div/div[1]")).getText();
                    String[] studySchedule = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[3]/div/ul/li[1]/div/ul/li[4]/div/div")).getText().split(" ~ ");
                    String studyStartDate = studySchedule[0];
                    String studyEndDate = studySchedule[1].substring(0, 10);
                    String dayTime = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[3]/div/ul/li[1]/div/ul/li[6]/div/ul/li/p")).getText();
                    String recruitmentQuota = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[3]/div/ul/li[1]/div/ul/li[8]/div/div")).getText();
                    String[] classMethod = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[3]/div/ul/li[2]/div/ul/li[2]/div/div/div")).getText().split("\n");
                    String learningEquipment = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[3]/div/ul/li[2]/div/ul/li[4]/div/div")).getText();
                    String classType = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[3]/div/ul/li[2]/div/ul/li[6]/div/div/div")).getText();
                    String studyPlace = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[3]/div/ul/li[2]/div/ul/li[8]/div/ul/div")).getText();
                    String cost = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[6]/div/ul/li/div/ul/li[2]/div/div")).getText();
                    String tomorrowLearningCard = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[6]/div/ul/li/div/ul/li[4]/div/span/span")).getText();
                    String[] subsidy = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li[6]/div/ul/li/div/ul/li[6]/div/div")).getText().split("\n");
                    Map<String, List<String>> curriculumMap = new HashMap<>();
                    WebElement curriculum = item.findElement(By.xpath("div[2]/ul/li[3]/ul/li/div/div[2]/div[1]"));
                    for (int i = 0; i < curriculum.findElements(By.cssSelector("h3")).size(); i++) {
                        List<String> curriculumList = new ArrayList<>();
                        for (int j = 0; j < curriculum.findElements(By.cssSelector("div")).get(i).findElements(By.cssSelector("ul/li")).size(); j++) {
                            curriculumList.add(curriculum.findElements(By.cssSelector("div")).get(i).findElements(By.cssSelector("ul/li")).get(j).getText());
                        }
                        curriculumMap.put(curriculum.findElements(By.cssSelector("h3")).get(i).getText(), curriculumList);
                    }

                    log.info("educationalName : {}", educationalName);
                    log.info("curriculumName : {}", curriculumName);
                    log.info("categoryList : {}", categoryList);
                    log.info("bootCampImageUrl : {}", bootCampImageUrl);
                    log.info("recruitmentEndDate : {}", recruitmentEndDate);
                    log.info("studyStartDate : {}", studyStartDate);
                    log.info("studyEndDate : {}", studyEndDate);
                    log.info("dayTime : {}", dayTime);
                    log.info("recruitmentQuota : {}", recruitmentQuota);
                    log.info("classMethod : {}", Arrays.toString(classMethod));
                    log.info("learningEquipment : {}", learningEquipment);
                    log.info("classType : {}", classType);
                    log.info("studyPlace : {}", studyPlace);
                    log.info("cost : {}", cost);
                    log.info("tomorrowLearningCard : {}", tomorrowLearningCard);
                    log.info("subsidy : {}", Arrays.toString(subsidy));
                    log.info("curriculumMap : {}", curriculumMap);

                    // CrawlingDTO 객체 생성 및 추가
                    // CrawlingDTO dto = CrawlingDTO.builder()....build();
                    // dtos.add(dto);

                } catch (Exception e) {
                    log.error("행 데이터를 추출하는 중 오류 발생 : ", e);
                } finally {
                    // 새 탭을 닫고 원래 탭으로 전환
                    webDriver.close();
                    webDriver.switchTo().window(originalTab);
                }
//            }

            // saveAll로 한번에 저장
            // crawlingRepository.saveAll(crawlingMapper.toEntities(dtos));

        } catch (Exception e) {
            log.error("크롤링 중 오류 발생: ", e);
        } finally {
            webDriver.quit();
        }
    }

    public void deleteCrawling() {
        crawlingRepository.deleteAll();
    }
}
