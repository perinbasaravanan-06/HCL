package com.hcl.backend.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalPrice;
    private int totalItems;

    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.ACTIVE;

    // 🔥 MERGED FIELD (IMPORTANT)
    @Lob
    private String itemsJson;

    private LocalDateTime updatedAt;

    // RELATION
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum CartStatus {
        ACTIVE,
        CHECKED_OUT
    }
}
