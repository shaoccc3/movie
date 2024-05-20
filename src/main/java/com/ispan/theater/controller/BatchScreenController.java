package com.ispan.theater.controller;

import com.ispan.theater.dto.SchduleDto;
import com.ispan.theater.service.BatchScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class BatchScreenController {
    @Autowired
    private BatchScreenService batchScreenService;
    @PostMapping("/backstage/screening-batch")
    public ResponseEntity<?> createBatchSchedule(@RequestBody SchduleDto request) {
        try {
            batchScreenService.batchScreenInsert(request);
            return ResponseEntity.ok("Batch schedule created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating batch schedule: " + e.getMessage());
        }
    }
}
