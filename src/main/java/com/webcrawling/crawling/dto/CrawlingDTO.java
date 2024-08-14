package com.webcrawling.crawling.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CrawlingDTO {
    private String curriculumName; // 교육과정명
    private String programCourse;  // 프로그램 과정
    private String recruitmentStatus; // 모집 상태
    private String cost;     // 비용
    private String learningMethod; // 학습방법
    private String studyStartDate; // 학습시작날짜
    private String studyEndDate;   // 학습종료날짜
    private String participationTime; // 참여 시간
    private String selectionProcedure; // 선발 절차
    private String keyword; // 키워드
    private String operatingCompany; // 운영기업
}
