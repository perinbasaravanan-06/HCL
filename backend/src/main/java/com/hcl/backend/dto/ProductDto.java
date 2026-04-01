package com.hcl.backend.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String size;
    private String imageUrl;
    private double rating;
    private boolean isAvailable;
    private String category;
}
