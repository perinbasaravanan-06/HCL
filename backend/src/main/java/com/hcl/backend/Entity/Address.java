package com.hcl.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String phone;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;

    private String landmark;

    private boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
