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
        options.addArguments("--start-maximized"); // 창 최대화
//        options.addArguments("--window-size=1200,600"); // 창 사이즈 조절
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito"); // 시크릿 모드
        options.addArguments("--disable-cache"); // 캐시 비활성화
        options.addArguments("--disable-popup-blocking"); // 팝업 무시
//        options.addArguments("headless"); // 창이없이 프로세스 사용
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
        return new ChromeDriver(options);
    }
}
