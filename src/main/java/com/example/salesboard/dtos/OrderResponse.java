package com.example.salesboard.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long id;

    private Long advertId;
    private String advertTitle;
    private BigDecimal advertPrice;

    private Long buyerId;
    private String buyerUsername;
    private String buyerCity;

    private Long sellerId;
    private String sellerUsername;
    private String sellerCity;

    private BigDecimal orderPrice;
    private LocalDateTime createdAt;
}