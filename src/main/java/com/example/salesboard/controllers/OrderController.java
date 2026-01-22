package com.example.salesboard.controllers;

import com.example.salesboard.dtos.CreateOrderRequest;
import com.example.salesboard.dtos.OrderResponse;
import com.example.salesboard.entities.Order;
import com.example.salesboard.mappers.OrderMapper;
import com.example.salesboard.repositories.AdvertRepository;
import com.example.salesboard.repositories.OrderRepository;
import com.example.salesboard.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@AllArgsConstructor
public class OrderController {
    private OrderMapper orderMapper;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private AdvertRepository advertRepository;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            UriComponentsBuilder uriBuilder,
            @RequestBody CreateOrderRequest request
    ){
        var advert = advertRepository.findById((long)7).orElse(null);
        if(advert == null){
            return ResponseEntity.notFound().build();
        }
        Order order = orderMapper.toEntity(request);
        order.setAdvert(advert);
        order.setBuyer(advert.getUser());
        order.setSeller(userRepository.findById((long)7).orElse(null));
        orderRepository.save(order);
        OrderResponse response = orderMapper.toDto(order);
        var uri = uriBuilder.path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        OrderResponse response = orderMapper.toDto(order);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        orderRepository.delete(order);
        return ResponseEntity.noContent().build();
    }
}
