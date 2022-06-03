package com.interview.katacodebusters.controllers;

import com.interview.katacodebusters.services.PricingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/portfolio")
@Slf4j
public class PricingController {
    private final PricingService pricingService;

    @Autowired
    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping(path = "/price", produces = "text/csv")
    public ResponseEntity<Resource> getCsvPrices() throws IOException {
        log.info("get csv that contains prices");
        InputStreamResource csvFileInputStream = pricingService.getCsvPrices();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= prices.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return ResponseEntity.ok().headers(headers).body(csvFileInputStream);
    }
}
