package com.interview.katacodebusters.controllers;

import com.interview.katacodebusters.dao.PricingDAO;
import com.interview.katacodebusters.models.Price;
import com.interview.katacodebusters.repositories.PriceRepo;
import com.interview.katacodebusters.services.PricingService;
import com.interview.katacodebusters.services.ReportingClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PricingControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test_ptf_prices_controller() throws Exception {
        try (MockedStatic<PricingService> pricingServiceMockedStatic = Mockito.mockStatic(PricingService.class)) {
            pricingServiceMockedStatic.when(PricingService::getCsvPrices)
                    .thenReturn(new InputStreamResource(new ByteArrayInputStream("test pricing controller".getBytes())));

            this.mockMvc.perform(get("/api/portfolio/price")).andExpect(status().isOk())
                    .andExpect(content().string("test pricing controller"));
        }

    }

    @Test
    public void test_reporting_client_controller() throws Exception {
        try (MockedStatic<ReportingClientService> reportingClientServiceMockedStatic = Mockito.mockStatic(ReportingClientService.class)) {
            reportingClientServiceMockedStatic.when(ReportingClientService::getReportClient)
                    .thenReturn(new InputStreamResource(new ByteArrayInputStream("test client report path".getBytes())));

            this.mockMvc.perform(get("/api/portfolio/client-report")).andExpect(status().isOk())
                    .andExpect(content().string("test client report path"));
        }

    }
}
