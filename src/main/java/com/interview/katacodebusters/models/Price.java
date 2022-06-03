package com.interview.katacodebusters.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Price {
    private String portfolio;
    private String product;
    private String underlying;
    private String currency;
    private double price;

}
