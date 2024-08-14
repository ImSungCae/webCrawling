package com.webcrawling.crawling.service;

import com.webcrawling.crawling.dto.CrawlingDTO;
import com.webcrawling.crawling.mapper.CrawlingMapper;
import com.webcrawling.crawling.repository.CrawlingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlingService {

    private final WebDriver webDriver; // 셀레니움 사용을 위한 webDriver 주입
    private final CrawlingRepository crawlingRepository;
    private final CrawlingMapper crawlingMapper;

    @Value("${crawling.url}")
    private String url; // 사이트 URL(YAML의 crawling.url에서 주입받음)

    public void crawling(){
        try{
            log.info("페이지 접속");
            webDriver.get(url);

            log.info("페이지 접속후 로딩 까지 대기(최대 10초)");
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10)); // 최대 10초까지 기다리기

            List<WebElement> elements = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.cssSelector("body > main > section > div.w-full.py-2 > div > section > div.relative.mb-5.mt-2\\.5.w-full.rounded-\\[10px\\] > div.w-ful.overflow-x-auto > table > tbody > tr")
                    )
            );


            JavascriptExecutor js = (JavascriptExecutor) webDriver;

            // 한번에 저장하기 위해 List 선언
            List<CrawlingDTO> dtos = new ArrayList<>();
            String originalTab = webDriver.getWindowHandle();

            WebElement element = elements.get(0);
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);

//            // 각 행 데이터를 추출
//            for (WebElement element : elements){
                try{
                    wait.until(ExpectedConditions.elementToBeClickable(element));
                    // 상세페이지 접속

                    js.executeScript("arguments[0].scrollIntoView(true);",element);

                    js.executeScript("arguments[0].click();",element);
                    element.click();
                    element.sendKeys(Keys.ENTER);


                    Set<String> allTabs = webDriver.getWindowHandles();
                    List<String> tabs = new ArrayList<>(allTabs);

                    webDriver.switchTo().window(tabs.get(tabs.size() - 1));

                    WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > main > section > section > div.w-full.lg\\:w-\\[calc\\(100\\%-345px\\)\\].xl\\:w-\\[calc\\(100\\%-385px\\)\\]")));
                    String bootCampImageUrl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > main > section > header > div.relative.grid.aspect-video.w-full.grow-0.border.border-grey-200.sm\\:aspect-\\[3\\/1\\].lg\\:aspect-\\[10\\/3\\].grid-cols-1.gap-1.overflow-hidden.rounded-none.md\\:gap-2.lg\\:gap-2\\.5.lg\\:rounded-\\[10px\\] > div > img\n"))).getAttribute("src");
                    String educationalName = item.findElement(By.cssSelector("div.flex.w-full.flex-col.items-start.text-semibold18.md\\:text-bold24 >div:nth-child(2)")).getText();
                    String curriculumName = item.findElement(By.cssSelector("div.flex.w-full.flex-col.items-start.text-semibold18.md\\:text-bold24 > div:nth-child(3)")).getText();
                    List<String> categoryList = new ArrayList<>();
                    List<WebElement> categoryWeb = item.findElements(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(1) > div > div.flex.w-full.flex-col.gap-\\[10px\\].text-semibold14.text-grey-400.md\\:text-semibold16 > div.rounded-\\[10px\\].bg-grey-100.p-4.md\\:p-5.mt-5.flex.flex-col.gap-2\\.5.text-grey-800 > ul > li"));
                    for (WebElement category : categoryWeb){
                        categoryList.add(category.findElement(By.cssSelector("span")).getText());
                    }
                    String recruitmentEndDate = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(3) > div > ul > li:nth-child(1) > div > ul > li:nth-child(2) > div > div > div:nth-child(1)")).getText();
                    String[] studySchedule = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(3) > div > ul > li:nth-child(1) > div > ul > li:nth-child(4) > div > div")).getText().split(" ~ ");
                    String studyStartDate = studySchedule[0];
                    String studyEndDate = studySchedule[1].substring(0,10);
                    String dayTime = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(3) > div > ul > li:nth-child(1) > div > ul > li:nth-child(6) > div > ul > li > p")).getText();
                    String recruitmentQuota = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(3) > div > ul > li:nth-child(1) > div > ul > li:nth-child(8) > div > div")).getText();
                    String[] classMethod = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(3) > div > ul > li:nth-child(2) > div > ul > li:nth-child(2) > div > div")).getText().split("/n");
                    String learningEquipment = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(3) > div > ul > li:nth-child(2) > div > ul > li:nth-child(4) > div > div")).getText();
                    String classType = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(3) > div > ul > li:nth-child(2) > div > ul > li:nth-child(6) > div > div > div")).getText();
                    String studyPlace = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(3) > div > ul > li:nth-child(2) > div > ul > li:nth-child(8) > div > ul > div")).getText();
                    String cost = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(4) > div > ul > li > div > ul > li:nth-child(2) > div > div")).getText();
                    String tomorrowLearningCard = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(4) > div > ul > li > div > ul > li:nth-child(4) > div > span > span")).getText();
                    String[] subsidy = item.findElement(By.cssSelector("div.relative.flex.w-full.flex-col.items-start.gap-\\[30px\\] > ul > li:nth-child(1) > ul > li:nth-child(4) > div > ul > li > div > ul > li:nth-child(6) > div > div")).getText().split("\n");

                    System.out.println(educationalName);
                    System.out.println(curriculumName);
                    System.out.println(categoryList);
                    System.out.println(bootCampImageUrl);
                    System.out.println(recruitmentEndDate);
                    System.out.println(studyStartDate);
                    System.out.println(studyEndDate);
                    System.out.println(dayTime);
                    System.out.println(recruitmentQuota);
                    System.out.println(Arrays.toString(classMethod));
                    System.out.println(learningEquipment);
                    System.out.println(classType);
                    System.out.println(studyPlace);
                    System.out.println(cost);
                    System.out.println(tomorrowLearningCard);
                    System.out.println(Arrays.toString(subsidy));



//                    String curriculumName = element.findElement(By.cssSelector("th > a > div.z-10.cursor-pointer.bg-white > div > div.flex.grow.flex-col.items-start.gap-\\[5px\\] > h2 > p.break-keep.text-semibold16.transition-all")).getText();
//                    String programCourse = element.findElement(By.cssSelector("td:nth-child(2) > div > a > ul > li:nth-child(1) > div")).getText();
//                    String recruitmentStatus = element.findElement(By.cssSelector("td:nth-child(3) > div > a > div > div.whitespace-pre-wrap.before\\:ml-0\\.5.before\\:text-grey-500.before\\:content-\\[\\'\\~\\'\\]")).getText();
//                    String cost = element.findElement(By.cssSelector("td:nth-child(4) > div > a > div")).getText();
//                    String learningMethod = element.findElement(By.cssSelector("td:nth-child(5) > div > a > div > div.whitespace-nowrap.rounded.px-\\[5px\\].py-0\\.5.text-regular13.undefined")).getText();
//                    String studyStartDate = element.findElement(By.cssSelector("td:nth-child(6) > div > a > div > span:nth-child(1)")).getText();
//                    String studyEndDate = element.findElement(By.cssSelector("td:nth-child(6) > div > a > div > span:nth-child(3)")).getText();
//                    String participationTime = element.findElement(By.cssSelector("td:nth-child(7) > div > a > div > ul")).getText();
//                    String selectionProcedure = element.findElement(By.cssSelector("td:nth-child(8) > div > a > div > div")).getText();
//                    String keyword = element.findElement(By.cssSelector("td:nth-child(8) > div > a > div > ul > li:nth-child(1) > div")).getText();
//                    String operatingCompany = element.findElement(By.cssSelector("td:nth-child(9) > div > ul > li > div > h2")).getText();
//
//                    CrawlingDTO dto = CrawlingDTO.builder()
//                            .curriculumName(curriculumName)
//                            .programCourse(programCourse)
//                            .recruitmentStatus(recruitmentStatus)
//                            .cost(cost)
//                            .learningMethod(learningMethod)
//                            .studyStartDate(studyStartDate)
//                            .studyEndDate(studyEndDate)
//                            .participationTime(participationTime)
//                            .selectionProcedure(selectionProcedure)
//                            .keyword(keyword)
//                            .operatingCompany(operatingCompany)
//                            .build();
//
//                    dtos.add(dto);
//                System.out.printf("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n",
//                        curriculumName,programCourse,recruitmentStatus,cost,learningMethod,studyStartDate,studyEndDate,participationTime,selectionProcedure,keyword,operatingCompany);
                } catch (Exception e) {
                    log.error("행 데이터를 추출하는 중 오류 발생 : ",e);
                } finally {
                    webDriver.close();
                    webDriver.switchTo().window(originalTab);
                }

//            }
            // saveAll로 한번에 저장 save로 하나하나 저장할때보다 성능적으로 유리
//            crawlingRepository.saveAll(crawlingMapper.toEntities(dtos));


            }catch (Exception e){
                log.error("크롤링 중 오류 발생: ",e);
            }finally {
                webDriver.quit();
            }
        }

    public void deleteCrawling(){
        crawlingRepository.deleteAll();
    }




}
