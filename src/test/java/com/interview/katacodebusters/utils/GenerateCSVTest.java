package com.interview.katacodebusters.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GenerateCSVTest {

    @Test
    public void test_generate_csv() throws IOException {
        var resultMap = Map.of("C1", 450.0);
        String header1 = "header1";
        String header2 = "header2";
        var actual = GenerateCSV.generateCSV(resultMap, header1, header2);
        var expected = new InputStreamResource(new ByteArrayInputStream("header1;header2\r\nC1;450.0\r\n".getBytes()));
        assertEquals(new String(actual.getInputStream().readAllBytes(), StandardCharsets.UTF_8),
                new String(expected.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
    }
}
