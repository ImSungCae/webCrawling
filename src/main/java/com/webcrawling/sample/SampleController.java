package com.webcrawling.sample;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/samples")
@Tag(name = "Sample CRUD")
public class SampleController {
    private final SampleService sampleService;

    @GetMapping
    public ResponseEntity<Page<Sample>> getSamples(Pageable pageable) {
        Page<Sample> samples = sampleService.findList(pageable);
        return new ResponseEntity<>(samples, HttpStatus.OK);
    }

    @GetMapping("/{seq}")
    public ResponseEntity<Sample> getSample(@PathVariable Long seq) {
        Sample sample = sampleService.find(seq);
        if (sample != null) {
            return new ResponseEntity<>(sample, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Sample> createSample(@RequestBody Sample sample) {
        Sample createdSample = sampleService.create(sample);
        return new ResponseEntity<>(createdSample, HttpStatus.CREATED);
    }

    @PutMapping("/{seq}")
    public ResponseEntity<Sample> updateSample(@PathVariable Long seq, @RequestBody Sample sample) {
        Sample existingSample = sampleService.find(seq);
        if (existingSample != null) {
            Sample updatedSample = sampleService.modify(sample);
            return new ResponseEntity<>(updatedSample, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> deleteSample(@PathVariable Long seq) {
        Sample existingSample = sampleService.find(seq);
        if (existingSample != null) {
            sampleService.remove(seq);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
