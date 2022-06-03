package com.interview.katacodebusters.services;

import com.interview.katacodebusters.dao.ForexDAO;
import com.interview.katacodebusters.dao.PricingDAO;
import com.interview.katacodebusters.dao.ProductDao;
import com.interview.katacodebusters.models.Price;
import com.interview.katacodebusters.models.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class PricingServiceTest {
    private static final List<Price> LIST_PRICES = List.of(Price.builder().price(5.0).currency("EUR").portfolio("PTF1").underlying("U11").product("P1").build(),
            Price.builder().price(4.0).currency("EUR").portfolio("PTF1").underlying("U12").product("P1").build(),
            Price.builder().price(8.0).currency("USD").portfolio("PTF2").underlying("X1").product("P2").build());
    private static final Map<String, Double> EXCHANGE_RATE_MAP = Map.of("EUR", 1.0, "USD", 0.5);
    private static final List<Product> PRODUCT_LIST = List.of(Product.builder().product("P1").client("C1").quantity(20).build(),
            Product.builder().product("P2").client("C1").quantity(30).build(),
            Product.builder().product("P1").client("C2").quantity(40).build());

    @Test
    public void test_compute_ptf_prices_map() throws IOException {
        try (MockedStatic<ForexDAO> forexDao = Mockito.mockStatic(ForexDAO.class);
             MockedStatic<ProductDao> productDao = Mockito.mockStatic(ProductDao.class);
             MockedStatic<PricingDAO> pricingDao = Mockito.mockStatic(PricingDAO.class)) {
            forexDao.when(ForexDAO::getRateExchangeMap)
                    .thenReturn(EXCHANGE_RATE_MAP);

            productDao.when(ProductDao::getProducts)
                    .thenReturn(PRODUCT_LIST);

            pricingDao.when(PricingDAO::getPortfolios)
                    .thenReturn(LIST_PRICES);

            Map<String, Double> expected = Map.of("PTF1", 540.0, "PTF2", 120.0);
            var actual = PricingService.computePTFPricesMap(EXCHANGE_RATE_MAP, PRODUCT_LIST);
            Assertions.assertEquals(expected, actual);
        }
    }
}
