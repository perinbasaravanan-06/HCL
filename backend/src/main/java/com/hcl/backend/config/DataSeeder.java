package com.hcl.backend.config;

import com.hcl.backend.Entity.Product;
import com.hcl.backend.Entity.User;
import com.hcl.backend.Entity.Coupon;
import com.hcl.backend.Entity.RawMaterial;
import com.hcl.backend.repository.CouponRepository;
import com.hcl.backend.repository.RawMaterialRepository;
import com.hcl.backend.repository.ProductRepository;
import com.hcl.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDefaultAdminAndProducts(
            UserRepository userRepository,
            ProductRepository productRepository,
            RawMaterialRepository rawMaterialRepository,
            CouponRepository couponRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            seedDefaultAdmin(userRepository, passwordEncoder);
            seedSampleProducts(productRepository);
            seedRawMaterials(rawMaterialRepository);
            seedSampleCoupons(couponRepository);
        };
    }

    private void seedRawMaterials(RawMaterialRepository rawMaterialRepository) {
        if (rawMaterialRepository.count() > 0) {
            return;
        }

        List<RawMaterial> materials = List.of(
                buildRawMaterial("Flour", RawMaterial.Unit.KG, 50.0, 10.0, "Global Grains"),
                buildRawMaterial("Cheese", RawMaterial.Unit.KG, 20.0, 5.0, "Dairy Delight"),
                buildRawMaterial("Tomato Sauce", RawMaterial.Unit.LITRE, 30.0, 5.0, "Sauce Masters"),
                buildRawMaterial("Veggie Mix", RawMaterial.Unit.KG, 15.0, 3.0, "Fresh Farm"),
                buildRawMaterial("Milk", RawMaterial.Unit.LITRE, 40.0, 10.0, "Dairy Delight")
        );

        rawMaterialRepository.saveAll(materials);
    }

    private RawMaterial buildRawMaterial(String name, RawMaterial.Unit unit, double qty, double reorder, String supplier) {
        RawMaterial material = new RawMaterial();
        material.setName(name);
        material.setUnit(unit);
        material.setQuantityAvailable(qty);
        material.setReorderLevel(reorder);
        material.setSupplierName(supplier);
        material.setLastUpdated(LocalDateTime.now());
        return material;
    }

    private void seedDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        final String adminEmail = "admin@gmail.com";
        User admin = userRepository.findByEmail(adminEmail).orElseGet(User::new);

        if (admin.getId() == null) {
            admin.setEmail(adminEmail);
            admin.setCreatedAt(LocalDateTime.now());
        }

        admin.setName("Default Admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setPhone("9999999999");
        admin.setRole("ADMIN");
        admin.setBlocked(false);
        admin.setUpdatedAt(LocalDateTime.now());

        userRepository.save(admin);
    }

    private void seedSampleProducts(ProductRepository productRepository) {
        if (productRepository.count() > 0) {
            return;
        }

        List<Product> products = List.of(
                buildProduct("Farmhouse Pizza", "Classic veggie pizza with cheese", 299.0, 20, "Pizza", "MEDIUM", "https://picsum.photos/seed/pizza1/400/300"),
                buildProduct("Paneer Tikka Pizza", "Paneer cubes with spicy tikka sauce", 349.0, 15, "Pizza", "MEDIUM", "https://picsum.photos/seed/pizza2/400/300"),
                buildProduct("Veg Burger", "Crispy veg patty with fresh lettuce", 149.0, 30, "Burger", "REGULAR", "https://picsum.photos/seed/burger1/400/300"),
                buildProduct("French Fries", "Golden crispy fries", 99.0, 40, "Snacks", "REGULAR", "https://picsum.photos/seed/fries1/400/300"),
                buildProduct("Cold Coffee", "Chilled coffee with ice cream", 129.0, 25, "Beverages", "REGULAR", "https://picsum.photos/seed/coffee1/400/300")
        );

        productRepository.saveAll(products);
    }

    private Product buildProduct(String name, String description, double price, int stock, String category, String size, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setSize(size);
        product.setAvailable(true);
        product.setImageUrl(imageUrl);
        product.setRating(4.2);
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }

    private void seedSampleCoupons(CouponRepository couponRepository) {
        if (couponRepository.count() > 0) return;

        Coupon save10 = new Coupon();
        save10.setCode("SAVE10");
        save10.setDiscountType(Coupon.DiscountType.PERCENT);
        save10.setDiscountValue(10);
        save10.setMinOrderAmount(199);
        save10.setMaxDiscount(100);
        save10.setExpiryDate(LocalDate.now().plusMonths(6));
        save10.setActive(true);
        save10.setUsageLimit(1000);
        save10.setUsedCount(0);

        Coupon flat50 = new Coupon();
        flat50.setCode("FLAT50");
        flat50.setDiscountType(Coupon.DiscountType.FLAT);
        flat50.setDiscountValue(50);
        flat50.setMinOrderAmount(299);
        flat50.setMaxDiscount(50);
        flat50.setExpiryDate(LocalDate.now().plusMonths(6));
        flat50.setActive(true);
        flat50.setUsageLimit(1000);
        flat50.setUsedCount(0);

        couponRepository.saveAll(List.of(save10, flat50));
    }
}

