package com.interview.katacodebusters.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class GenerateCSV {
    public static InputStreamResource generateCSV(Map<String, Double>resultMap, String header1, String header2) {
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader(header1, header2)
                .setDelimiter(';')
                .build();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter;
        try {
            csvPrinter = new CSVPrinter(new PrintStream(out, true, StandardCharsets.UTF_8.toString()), csvFormat);
            resultMap.forEach((key, value) -> {
                try {
                    csvPrinter.printRecord(key, value);
                } catch (IOException e) {
                    log.error("error while adding client report in output file", e);
                }
            });
            csvPrinter.flush();
        } catch (IOException e) {
            log.error("error while creating csv result file for client report", e);
        }

        return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
    }
}
