package com.example.salesboard.repositories;

import com.example.salesboard.entities.Advert;
import com.example.salesboard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    @EntityGraph(attributePaths = {"category"})
    List<Advert> findByUserOrderByCreatedAtDesc(User user);
}
