package com.webcrawling.crawling.service;

import com.webcrawling.crawling.dto.CrawlingDTO;
import com.webcrawling.crawling.mapper.CrawlingMapper;
import com.webcrawling.crawling.repository.CrawlingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlingTestService {

    private final WebDriver webDriver;
    private final CrawlingRepository crawlingRepository;
    private final CrawlingMapper crawlingMapper;

    @Value("${crawling.url}")
    private String url;

    public void crawling() {
        try {
            log.info("페이지 접속");
            webDriver.get(url);

            log.info("페이지 로딩 대기(최대 10초)");
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            List<WebElement> categorys = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("/html/body/main/section/div[1]/div/ul/li")
            ));



            JavascriptExecutor js = (JavascriptExecutor) webDriver;
//            List<CrawlingDTO> dtos = new ArrayList<>();


//            for (int i = 1; i < categorys.size(); i++) {
                WebElement category = categorys.get(1);
                wait.until(ExpectedConditions.elementToBeClickable(category)).click();
                while(true) {
                    try {
                        Thread.sleep(1000);
                        WebElement div_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/main/section/div[7]/div/section/div[3]/div")));
                        WebElement btn = div_btn.findElement(By.xpath("./button"));

                        js.executeScript("arguments[0].click();", btn);
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                        Thread.sleep(1000);

                    } catch (NoSuchElementException e) {
                        log.info("더 이상 버튼이 없습니다. 루프 종료.");
                        break;
                    } catch (TimeoutException e) {
                        log.info("타임아웃 발생 - 버튼을 찾을 수 없습니다.");
                        break;
                    } catch (StaleElementReferenceException e) {
                        log.info("StaleElementReferenceException 발생 - 요소를 다시 찾습니다.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("버튼 로드 중 에러 : {}", e.getMessage());
                        break;
                    }
                }
                List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("/html/body/main/section/div[7]/div/section/div[2]/div[2]/table/tbody/tr")
                ));
                String originalTab = webDriver.getWindowHandle();
                for (WebElement element : elements){
                    try {
                        js.executeScript("window.scrollTo(0, 0);");
                        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                        js.executeScript("arguments[0].click();", wait.until(ExpectedConditions.elementToBeClickable(element.findElement(By.xpath("./th//a")))));

                        Set<String> allTabs = webDriver.getWindowHandles();
                        for (String tab : allTabs) {
                            if (!tab.equals(originalTab)) {
                                webDriver.switchTo().window(tab);
                                break;
                            }
                        }

                        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

                        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("/html/body/main/section/section/div[1]")
                        ));

                        // 대표 이미지의 XPath 목록
                        String[] imageXPaths = {
                                "/html/body/main/section/header/div[2]/div[1]/button/img",
                                "/html/body/main/section/header/div[2]/div/img"
                        };

                        String bootCampImageUrl = null;
                        for (String xpath : imageXPaths) {
                            try {
                                bootCampImageUrl = webDriver.findElement(By.xpath(xpath)).getAttribute("src");
                                log.info("대표 이미지를 찾았습니다: {}", xpath);
                                break; // 이미지가 발견되면 루프 종료
                            } catch (NoSuchElementException e) {
                                log.info("XPath {} 에서 이미지를 찾을 수 없습니다.", xpath);
                            }
                        }

                        String educationalName = item.findElement(By.xpath("div[1]/div[2]")).getText();
                        String curriculumName = item.findElement(By.xpath("div[1]/div[3]")).getText();
                        List<String> categoryList = new ArrayList<>();
                        List<WebElement> categoryWeb = item.findElements(By.xpath("div[2]/ul/li[1]/ul/li[1]/div/div[2]/div[4]/ul/li"));
                        for (WebElement curriculumCategory : categoryWeb) {
                            categoryList.add(curriculumCategory.findElement(By.xpath("span")).getText());
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
                        String cost = item.findElement(By.xpath("div[2]/ul/li[1]/ul//div/ul/li/div/ul/li[2]/div/div")).getText();
                        String tomorrowLearningCard = item.findElement(By.xpath("div[2]/ul/li[1]/ul/li/div/ul/li/div/ul/li[4]/div/span/span")).getText();
                        String[] subsidy = item.findElement(By.xpath("div[2]/ul/li[1]/ul//div/ul/li/div/ul/li[6]/div/div")).getText().split("\n");
                        Map<String, List<String>> curriculumMap = new HashMap<>();
                        WebElement curriculum = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/section/section/div[1]/div[2]/ul/li[3]/ul/li/div/div[2]/div[1]")));
                        for (int i = 0; i < curriculum.findElements(By.xpath("h3")).size(); i++) {
                            List<String> curriculumList = new ArrayList<>();
                            for (int j = 0; j < curriculum.findElements(By.xpath("div")).get(i).findElements(By.xpath("ul/li")).size(); j++) {
                                curriculumList.add(curriculum.findElements(By.xpath("div")).get(i).findElements(By.xpath("ul/li")).get(j).getText());
                            }
                            curriculumMap.put(curriculum.findElements(By.xpath("h3")).get(i).getText(), curriculumList);
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


                    }catch (StaleElementReferenceException e){
                        log.info("StaleElementReferenceException 발생 - 요소를 다시 찾습니다.");
                    }catch (Exception e) {
                        log.error("상세 페이지 데이터 추출 중 오류 발생",e);
                    }finally {
                        webDriver.close();
                        webDriver.switchTo().window(originalTab);
                    }

                }



//            }


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
