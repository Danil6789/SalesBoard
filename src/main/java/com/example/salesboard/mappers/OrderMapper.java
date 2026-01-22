package com.example.salesboard.mappers;

import com.example.salesboard.dtos.CreateOrderRequest;
import com.example.salesboard.dtos.OrderResponse;
import com.example.salesboard.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "advert.id", target = "advertId")
    OrderResponse toDto(Order order);
    Order toEntity(CreateOrderRequest request);
}
