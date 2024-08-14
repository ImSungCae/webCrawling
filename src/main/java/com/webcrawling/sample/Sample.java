package com.webcrawling.sample;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Sample {
    private Long seq;
    private String name;

}
