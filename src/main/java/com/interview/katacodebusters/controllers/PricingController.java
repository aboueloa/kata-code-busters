package com.interview.katacodebusters.controllers;

import com.interview.katacodebusters.services.PricingService;
import com.interview.katacodebusters.services.ReportingClientService;
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

    @GetMapping(path = "/price", produces = "text/csv")
    public ResponseEntity<Resource> getCsvPrices() throws IOException {
        log.info("get csv that contains prices");
        InputStreamResource csvFileInputStream = PricingService.getCsvPrices();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= prices.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return ResponseEntity.ok().headers(headers).body(csvFileInputStream);
    }

    @GetMapping(path = "/client-report", produces = "text/csv")
    public ResponseEntity<Resource> getClientReport() throws IOException {
        log.info("get csv that contains client report");
        InputStreamResource csvFileInputStream = ReportingClientService.getReportClient();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= report-client.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return ResponseEntity.ok().headers(headers).body(csvFileInputStream);
    }
}
