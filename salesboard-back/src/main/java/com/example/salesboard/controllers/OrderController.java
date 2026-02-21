package com.example.salesboard.controllers;

import com.example.salesboard.dtos.CreateOrderRequest;
import com.example.salesboard.dtos.OrderResponse;
import com.example.salesboard.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            UriComponentsBuilder uriBuilder,
            @RequestBody CreateOrderRequest request,
            Authentication authentication
    ) {
        OrderResponse response = orderService.createOrder(request, authentication);
        var uri = uriBuilder.path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        return orderService.getMyOrders(authentication);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        OrderResponse response = orderService.getOrderById(id, authentication);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}