package com.interview.katacodebusters.dao;

import com.interview.katacodebusters.models.Price;
import com.interview.katacodebusters.models.Product;
import com.interview.katacodebusters.repositories.PriceRepo;
import com.interview.katacodebusters.repositories.ProductRepo;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductDaoTest {
    @Test
    public void test_get_products() throws IOException {
        try (MockedStatic<ProductRepo> productCSV = Mockito.mockStatic(ProductRepo.class)) {
            productCSV.when(ProductRepo::getProductCSV)
                    .thenReturn("src/test/resources/product-test.csv");

            var actualRes = ProductDao.getProducts();
            assertThat(actualRes).extracting(Product::getClient).containsExactlyInAnyOrder("C1", "C2");
            assertThat(actualRes).extracting(Product::getProduct).containsExactlyInAnyOrder("P1", "P2");
            assertThat(actualRes).extracting(Product::getQuantity).containsExactlyInAnyOrder(80, 10);
        }
    }
}
