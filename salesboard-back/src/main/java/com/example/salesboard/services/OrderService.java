package com.example.salesboard.services;

import com.example.salesboard.dtos.CreateOrderRequest;
import com.example.salesboard.dtos.OrderResponse;
import com.example.salesboard.entities.Advert;
import com.example.salesboard.entities.Order;
import com.example.salesboard.entities.User;
import com.example.salesboard.mappers.OrderMapper;
import com.example.salesboard.repositories.AdvertRepository;
import com.example.salesboard.repositories.OrderRepository;
import com.example.salesboard.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    public OrderResponse createOrder(CreateOrderRequest request, Authentication authentication) {
        Long currentUserId = Long.valueOf(authentication.getName());
        User buyer = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Advert advert = advertRepository.findById(request.getAdvertId())
                .orElseThrow(() -> new EntityNotFoundException("Advert not found with id: " + request.getAdvertId()));

        if (advert.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("Cannot buy your own advert");
        }

        Order order = orderMapper.toEntity(request);
        order.setAdvert(advert);
        order.setBuyer(buyer);
        order.setSeller(advert.getUser());
        order.setPrice(advert.getPrice());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    public OrderResponse getOrderById(Long id, Authentication authentication) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        Long currentUserId = Long.valueOf(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Проверяем права: админ или участник заказа
        if (!isAdmin &&
                !order.getBuyer().getId().equals(currentUserId) &&
                !order.getSeller().getId().equals(currentUserId)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }

        return orderMapper.toDto(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @PreAuthorize("isAuthenticated()")
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        Long currentUserId = Long.valueOf(authentication.getName());
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return orderRepository.findByBuyerOrSeller(user, user).stream()
                .map(orderMapper::toDto)
                .toList();
    }
}