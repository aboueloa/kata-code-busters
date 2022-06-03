package com.interview.katacodebusters.dao;

import com.interview.katacodebusters.repositories.ForexRepo;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ForexDaoTest {

    @Test
    public void test_get_forex_should_return_a_map_with_3_keys() throws IOException {
        try (MockedStatic<ForexRepo> forexCSV = Mockito.mockStatic(ForexRepo.class)) {
            forexCSV.when(ForexRepo::getForexPath)
                    .thenReturn("src/test/resources/forex-test.csv");

            var actualRes = ForexDAO.getRateExchangeMap();
            assertThat(actualRes.keySet()).containsExactlyInAnyOrder("EUR", "USD", "CHF");
            assertThat(actualRes.values()).containsExactlyInAnyOrder(0.25, 1.0, 4.0);
            assertThat(actualRes.get("EUR")).isEqualTo(1.0);
            assertThat(actualRes.get("USD")).isEqualTo(0.25);
            assertThat(actualRes.get("CHF")).isEqualTo(4.0);
        }
    }

    @Test
    public void test_get_forex_should_throw_an_IOException() {
        try (MockedStatic<ForexRepo> forexCSV = Mockito.mockStatic(ForexRepo.class)) {
            forexCSV.when(ForexRepo::getForexPath)
                    .thenReturn("src/test/resources/forex-non-existent.csv");

            Exception exception = assertThrows(IOException.class, ForexDAO::getRateExchangeMap);

            String expectedMessage = "java.nio.file.NoSuchFileException: src\\test\\resources\\forex-non-existent.csv";
            String actualMessage = exception.toString();

            assertEquals(actualMessage, expectedMessage);
        }

    }


}
