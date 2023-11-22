package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Order;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class ChartServiceImp implements ChartService{

    @Override
    public List<Double> generateDailyRevenue(List<Order> orders) {

            HashMap<String, Double> map = new HashMap<>();

            for (int i = 0; i < 7; i++) {
                map.put(LocalDate.now().minusDays(i).getDayOfWeek().toString(), 0.0);
            }

            for (Order item : orders) {
                if (item.getOrderDate().isAfter(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()))) {
                    map.put(item.getOrderDate().getDayOfWeek().toString(),
                            map.get(item.getOrderDate().getDayOfWeek().toString()) + item.getTotalPrice());
                }
            }

            List<Double> revenue = new ArrayList<>();

            for (int i = 1; i < 8; i++) {
                revenue.add(map.get(DayOfWeek.of(i).toString()));
            }

            return revenue;

    }

    public List<Double> generateMonthlyRevenue(List<Order> orders) {
        HashMap<String, Double> map = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            map.put(Integer.toString(i), 0.0);
        }

        for (Order item : orders) {
            if (item.getOrderDate().getMonthValue() == YearMonth.now().getMonthValue()) {
                map.put(Integer.toString(item.getOrderDate().getMonthValue()),
                        map.get(Integer.toString(item.getOrderDate().getMonthValue())) + item.getTotalPrice());
            }
        }

        List<Double> revenue = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            revenue.add(map.get(Integer.toString(i)));
        }

        return revenue;
    }

    public List<Double> generateYearlyRevenue(List<Order> orders) {
        HashMap<String, Double> map = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            map.put(Integer.toString(2020 + i), 0.0);
        }

        for (Order item : orders) {
            String year = Integer.toString(item.getOrderDate().getYear());
            if (map.containsKey(year)) {
                map.put(year, map.get(year) + item.getTotalPrice());
            } else {
                map.put(year, item.getTotalPrice());
            }
        }

        List<Double> revenue = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            revenue.add(map.get(Integer.toString(2020 + i)));
        }

        return revenue;
    }

}
