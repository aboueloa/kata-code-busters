package com.interview.katacodebusters.services;

import com.interview.katacodebusters.dao.ForexDAO;
import com.interview.katacodebusters.dao.PricingDAO;
import com.interview.katacodebusters.dao.ProductDao;
import com.interview.katacodebusters.models.Price;
import com.interview.katacodebusters.utils.GenerateCSV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Service
@Slf4j
public class ReportingClientService {

    public static InputStreamResource getReportClient() throws IOException {
        var exchangeRate = ForexDAO.getRateExchangeMap();
        var listPrices = PricingDAO.getPortfolios();
        var mapClientPrices = computeClientPricesMap(exchangeRate, listPrices);
        return GenerateCSV.generateCSV(mapClientPrices, "Client", "Capital");
    }

    protected static Map<String, Double> computeClientPricesMap(Map<String, Double> exchangeRate, List<Price> listPrices) throws IOException {
        return ProductDao.getProducts().stream()
                .flatMap(
                        p -> listPrices.stream()
                                .filter(price -> price.getProduct().equals(p.getProduct()))
                                .map(price -> List.of(p.getClient(), p.getProduct(), p.getQuantity() * price.getPrice() * exchangeRate.get(price.getCurrency())))
                )
                .collect(groupingBy(list -> (String) list.get(0), summingDouble(list -> (Double) list.get(2))));
    }
}
