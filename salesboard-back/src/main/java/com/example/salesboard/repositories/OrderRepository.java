package com.example.salesboard.repositories;

import com.example.salesboard.entities.Order;
import com.example.salesboard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerOrSeller(User buyer, User seller);
    List<Order> findByBuyer(User buyer);
    List<Order> findBySeller(User seller);
}
