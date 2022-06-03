package com.interview.katacodebusters.dao;

import com.interview.katacodebusters.repositories.ForexRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ForexDAO {

    public static Map<String, Double> getRateExchangeMap() throws IOException {
        String forexCSV = ForexRepo.getForexPath();
        var reader = Files.newBufferedReader(Paths.get(forexCSV));
        Map<String, Double> exchangeRate = new HashMap<>();
        exchangeRate.put("EUR", 1.0);
        try (reader) {
            var records = CSVFormat.DEFAULT.parse(reader);
            for(CSVRecord record: records){
                if(record.getRecordNumber() != 1){
                    if(record.get(0).equals("EUR")){
                        exchangeRate.put(record.get(1), 1/Double.parseDouble(record.get(2).replace(",", ".")));
                    }
                    else {
                        exchangeRate.put(record.get(0), Double.parseDouble(record.get(2)));
                    }
                }
            }
        } catch (IOException ex) {
            log.error("error while converting csv to portfolio object", ex);
        }
        return exchangeRate;
    }
}
