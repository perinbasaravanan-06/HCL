package com.hcl.backend.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardDto {
    private long totalUsers;
    private long totalOrders;
    private double totalRevenue;
    private long pendingOrders;
    private Map<String, Double> revenueByDay;
    private Map<String, Double> revenueByWeek;
    private Map<String, Double> revenueByMonth;
    private List<ProductDto> topProducts;
    private Map<String, Long> ordersByStatus;
}
