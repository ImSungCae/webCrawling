package com.webcrawling.sample;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "ba1_sample")
public class SampleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String name;
}