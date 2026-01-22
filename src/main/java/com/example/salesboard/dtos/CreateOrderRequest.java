package com.example.salesboard.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CreateOrderRequest {
    @NotNull(message = "ID объявления обязательно")
    private Long advertId;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;
}