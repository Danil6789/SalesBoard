package com.example.salesboard.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Favorite.FavoriteId.class)  // <-- ОБЯЗАТЕЛЬНО!
public class Favorite {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Include
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advert_id", nullable = false)
    @EqualsAndHashCode.Include
    private Advert advert;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Favorite(User user, Advert advert) {
        this.user = user;
        this.advert = advert;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteId implements Serializable {
        private Long user;
        private Long advert;
    }
}