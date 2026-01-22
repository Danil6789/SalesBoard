package com.example.salesboard.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name="username", nullable = false, length = 50)
    private String username;

    @Column(name="email", unique = true, nullable = false, length = 50)
    private String email;

    private String password;

    @Column(name="city", length = 30)
    private String city;

    @Column(name="phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;
}
