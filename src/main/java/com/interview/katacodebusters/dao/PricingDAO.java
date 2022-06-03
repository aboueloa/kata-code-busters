package com.interview.katacodebusters.dao;

import com.interview.katacodebusters.models.Price;
import com.interview.katacodebusters.repositories.PriceRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PricingDAO {

    public static List<Price> getPortfolios() throws IOException {
        String pricesCSV = PriceRepo.getPriceCSV();
        var reader = Files.newBufferedReader(Paths.get(pricesCSV));
        List<Price> prices = new ArrayList<>();
        try (reader) {
            var records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                if(record.getRecordNumber() != 1){
                    prices.add(
                            Price.builder()
                                    .portfolio(record.get(0))
                                    .product(record.get(1))
                                    .underlying(record.get(2))
                                    .price(Double.parseDouble(record.get(4)))
                                    .currency(record.get(3))
                                    .build()
                    );
                }
            }
        } catch (IOException ex) {
            log.error("error while converting csv to portfolio object", ex);
        }
        return prices;
    }

}
