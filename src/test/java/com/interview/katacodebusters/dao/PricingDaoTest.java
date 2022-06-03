package com.interview.katacodebusters.dao;

import com.interview.katacodebusters.models.Price;
import com.interview.katacodebusters.repositories.PriceRepo;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PricingDaoTest {

    @Test
    public void test_get_portfolios_should_return_a_list_of_ptfs() throws IOException {
        try (MockedStatic<PriceRepo> pricingCSV = Mockito.mockStatic(PriceRepo.class)) {
            pricingCSV.when(PriceRepo::getPriceCSV)
                    .thenReturn("src/test/resources/prices-test.csv");

            var actualRes = PricingDAO.getPortfolios();
            assertThat(actualRes).extracting(Price::getPortfolio).containsExactlyInAnyOrder("PTF1", "PTF2");
            assertThat(actualRes).extracting(Price::getPrice).containsExactlyInAnyOrder(10.0, 20.0);
            assertThat(actualRes).extracting(Price::getProduct).containsExactlyInAnyOrder("P1", "X1");
            assertThat(actualRes).extracting(Price::getUnderlying).containsExactlyInAnyOrder("U11", "UX1");
            assertThat(actualRes).extracting(Price::getCurrency).containsExactlyInAnyOrder("EUR", "USD");
        }
    }
}
