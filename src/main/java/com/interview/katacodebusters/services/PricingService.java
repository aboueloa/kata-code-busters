package com.interview.katacodebusters.services;

import com.interview.katacodebusters.dao.ForexDAO;
import com.interview.katacodebusters.dao.PricingDAO;
import com.interview.katacodebusters.dao.ProductDao;
import com.interview.katacodebusters.models.Price;
import com.interview.katacodebusters.models.Product;
import com.interview.katacodebusters.utils.GenerateCSV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Service
@Slf4j
public class PricingService {

    public static InputStreamResource getCsvPrices() throws IOException {
        var exchangeRate = ForexDAO.getRateExchangeMap();
        var listProduct = ProductDao.getProducts();
        var mapPtfPrices = computePTFPricesMap(exchangeRate, listProduct);
        return GenerateCSV.generateCSV(mapPtfPrices, "PTF", "Price");

    }

    protected static Map<String, Double> computePTFPricesMap(Map<String, Double> exchangeRate, List<Product> listProduct) throws IOException {
        return PricingDAO.getPortfolios().stream()
                        .flatMap(
                                ptf -> listProduct.stream()
                                        .filter(product -> product.getProduct().equals(ptf.getProduct()))
                                        .map(product -> List.of(ptf.getPortfolio(), product.getProduct(), product.getQuantity()*ptf.getPrice()*exchangeRate.get(ptf.getCurrency())))
                        )
                .collect(groupingBy(list -> (String) list.get(0), summingDouble(list -> (Double) list.get(2))));

    }

}
