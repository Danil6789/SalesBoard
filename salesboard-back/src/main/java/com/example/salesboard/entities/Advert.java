package com.example.salesboard.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Advert {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        @EqualsAndHashCode.Include
        private Long id;

        @Column(name = "title", nullable = false, length = 200)
        private String title;

        @Column(name = "description")
        private String description;

        @Column(name = "price", precision = 12, scale = 2)
        private BigDecimal price;


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        private Category category;


        @Enumerated(EnumType.STRING)
        @Column(name = "type")
        private AdvertType type;

        @Column(name = "city", length = 50)
        private String city;

        @Column(name = "image_url")
        private String imageUrl;

        @Column(name = "views_count")
        @Builder.Default
        private Integer viewsCount = 0;

        @Column(name = "status")
        @Enumerated(EnumType.STRING)
        @Builder.Default
        private AdvertStatus status = AdvertStatus.ACTIVE;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;


        @OneToMany(mappedBy = "advert", fetch = FetchType.LAZY)
        @Builder.Default
        private List<Order> orders = new ArrayList<>();

        @OneToMany(mappedBy = "advert", fetch = FetchType.LAZY)
        @Builder.Default
        private List<Favorite> favorites = new ArrayList<>();
}
