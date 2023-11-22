package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Order;

import java.util.List;

public interface ChartService {
    List<Double> generateDailyRevenue(List<Order> orders);

    List<Double> generateMonthlyRevenue(List<Order> orders);

    List<Double> generateYearlyRevenue(List<Order> orders);
}
