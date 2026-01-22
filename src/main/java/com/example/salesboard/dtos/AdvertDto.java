package com.example.salesboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class AdvertDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private String city;
    private String type;
    private String status;
    private String imageUrl;
}
