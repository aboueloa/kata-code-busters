package com.interview.katacodebusters.services;

import com.interview.katacodebusters.dao.ForexDAO;
import com.interview.katacodebusters.dao.PricingDAO;
import com.interview.katacodebusters.models.Price;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Slf4j
public record PricingService(ForexDAO forexDAO,
                             PricingDAO pricingDAO) {

    public InputStreamResource getCsvPrices() throws IOException {
        var exchangeRate = forexDAO.getRateExchangeMap();
        var listPrice = pricingDAO.getPortfolios();
        for(Price p : listPrice){
            p.setPrice( exchangeRate.get(p.getCurrency()) * p.getPrice());
        }
        var mapPtfPrices = listPrice.stream().collect(groupingBy(Price::getPortfolio,
                Collectors.summingDouble(Price::getPrice)));
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader("Portfolio", "prices")
                .setDelimiter(';')
                .build();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter;
        try {
            csvPrinter = new CSVPrinter(new PrintStream(out, true, StandardCharsets.UTF_8.toString()), csvFormat);
            mapPtfPrices.forEach((key, value) -> {
                try {
                    csvPrinter.printRecord(key, value);
                } catch (IOException e) {
                    log.error("error while adding prices in output file", e);
                }
            });
            csvPrinter.flush();
        } catch (IOException e) {
            log.error("error while creating csv result file", e);
        }

        return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

    }

}
