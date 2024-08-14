package com.webcrawling.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {
    @Bean
    public WebDriver webDriver(){
        WebDriverManager.chromedriver().driverVersion("127.0.6533.119").setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito"); // 시크릿 모드
        options.addArguments("--disable-cache"); // 캐시 비활성화
        options.addArguments("--disable-popup-blocking"); // 팝업 무시
//        options.addArguments("headless"); // 창이없이 프로세스 사용
        return new ChromeDriver(options);
    }
}
