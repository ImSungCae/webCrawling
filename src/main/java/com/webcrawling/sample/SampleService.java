package com.webcrawling.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final SampleRepository sampleRepository;
    private final SampleMapper sampleMapper;

    public Sample find(Long seq) {
        return sampleMapper.fromEntity(sampleRepository.findById(seq).orElse(null));
    }

    public Page<Sample> findList(Pageable pageable) {
        Page<SampleEntity> entityPage = sampleRepository.findAll(pageable);
        return entityPage.map(sampleMapper::fromEntity);
    }

    public Sample create(Sample sample) {
        return sampleMapper.fromEntity(
                sampleRepository.save(sampleMapper.toEntity(sample))
        );
    }

    public Sample modify(Sample sample) {
        return sampleMapper.fromEntity(
                sampleRepository.save(sampleMapper.toEntity(sample))
        );
    }

    public void remove(Long seq) {
        sampleRepository.deleteById(seq);
    }
}