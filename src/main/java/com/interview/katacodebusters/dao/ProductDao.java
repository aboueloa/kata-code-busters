package com.interview.katacodebusters.dao;

import com.interview.katacodebusters.models.Product;
import com.interview.katacodebusters.repositories.ProductRepo;
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
public class ProductDao {
    public static List<Product> getProducts() throws IOException {
        String productCSV = ProductRepo.getProductCSV();
        var reader = Files.newBufferedReader(Paths.get(productCSV));
        List<Product> products = new ArrayList<>();
        try (reader) {
            var records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                if(record.getRecordNumber() != 1){
                    products.add(
                            Product.builder()
                                    .product(record.get(0))
                                    .client(record.get(1))
                                    .quantity(Integer.parseInt(record.get(2)))
                                    .build()
                    );
                }
            }
        } catch (IOException ex) {
            log.error("error while converting csv to portfolio object", ex);
        }
        return products;
    }
}
