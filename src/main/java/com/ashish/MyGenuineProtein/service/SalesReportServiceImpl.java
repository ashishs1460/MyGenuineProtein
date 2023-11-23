package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.OrderItems;
import com.ashish.MyGenuineProtein.model.SalesReportTable;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesReportServiceImpl implements SalesReportService{

    @Autowired
    OrderService orderService;
    @Override
    public List<Order> filterOrders(String dateFilter) {
        List<Order> allOrders = orderService.findAll();
        List<Order> filteredUserOrders = new ArrayList<>(allOrders
                .stream()
                .filter(order -> !order.getStatus().name().equals("CANCELLED")).toList());
        Collections.reverse(filteredUserOrders);
        List<Order> orders = new ArrayList<>();
        LocalDate  today = LocalDate.now();

        switch (dateFilter) {
            case "DAILY" -> orders = filterDailyOrders(filteredUserOrders, today);
            case "WEEKLY" -> orders = filterWeeklyOrders(filteredUserOrders, today);
            case "MONTHLY" -> orders = filterMonthlyOrders(filteredUserOrders, today);
            case "YEARLY" -> orders = filterYearlyOrders(filteredUserOrders, today);
            case "ALL" -> orders = filteredUserOrders;
        }

        return orders;
    }

    @Override
    public byte[] exportPdfReport(String format) throws JRException {
        List<Order> userOrders = orderService.findAll();
        List<Order> filteredUserOrders = new ArrayList<>(userOrders
                .stream()
                .filter(order -> !order.getStatus().name().equals("CANCELLED")).toList());

        Collections.reverse(filteredUserOrders);

        List<SalesReportTable> salesReport = new ArrayList<>();
        for (Order order :filteredUserOrders) {
            SalesReportTable report = new SalesReportTable();
            report.setOrderId(order.getOrderId());
            report.setOrderDate(order.getOrderDate());
            report.setCustomer(order.getUser().getEmail());
            report.setTotalAmount(order.getTotalPrice());
            report.setPaymentMethod(order.getPaymentMode().name());
            List<OrderItems> orderItems = order.getOrderItems();

            for (int i=0; i<orderItems.size();i++) {
                if (i==0)
                    report.setProducts(orderItems.get(i).getVariant().getProduct().getName() + " - " + orderItems.get(i).getQuantity() + " nos.");
                else
                    report.setProducts(report.getProducts() + ", " + orderItems.get(i).getVariant().getProduct().getName() + " - " + orderItems.get(i).getQuantity() + " nos.");
            }

            report.setOrderStatus(order.getStatus().name());
            salesReport.add(report);

        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("grandTotal", Math.round(filteredUserOrders.stream().map(Order::getTotalPrice).reduce(0.0, Double::sum)*100.0)/100.0);
        parameters.put("quantity", filteredUserOrders.stream().map(Order::getTotalQuantity).reduce(0, Integer::sum));
        parameters.put("totalOrders", filteredUserOrders.size());


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(salesReport);
        InputStream stream = getClass().getClassLoader().getResourceAsStream("salesreport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(stream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);

        //Export to pdf
        if (format.equalsIgnoreCase("pdf")) {
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdfBytes;
        }
        //Export to xls
        if (format.equalsIgnoreCase("xlsx")) {

            JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(salesReport);
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
            JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, dataSource2);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint2);
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream);
            exporter.exportReport();
            return outputStream.toByteArray();
        }



            return new byte[0];
    }

    @Override
    public byte[] exportPdfReportForDate(LocalDate startDate, LocalDate endDate, String format) throws JRException {

        List<Order> userOrders = orderService.findAll();
        List<Order> filteredUserOrders = new ArrayList<>(userOrders
                .stream()
                .filter(order -> !order.getStatus().name().equals("CANCELLED")).toList());

        Collections.reverse(filteredUserOrders);
        List<Order> dateOrders = new ArrayList<>();
        for (Order order : filteredUserOrders) {
            LocalDate localDate = order.getOrderDate();
            if (localDate.isAfter(startDate.minusDays(1)) && localDate.isBefore(endDate.plusDays(1))) {
                dateOrders.add(order);
            }
            
        }

        List<SalesReportTable> salesReport = new ArrayList<>();
        for (Order order : dateOrders) {
            SalesReportTable report = new SalesReportTable();
            report.setOrderId(order.getOrderId());
            report.setOrderDate(order.getOrderDate());
            report.setCustomer(order.getUser().getEmail());
            report.setTotalAmount(order.getTotalPrice());
            report.setPaymentMethod(order.getPaymentMode().name());
            List<OrderItems> orderItemList = order.getOrderItems();

            for (int i=0; i<orderItemList.size();i++) {
                if (i==0)
                    report.setProducts(orderItemList.get(i).getVariant().getProduct().getName() + " - " + orderItemList.get(i).getQuantity() + " nos.");
                else
                    report.setProducts(report.getProducts() + ", " + orderItemList.get(i).getVariant().getProduct().getName() + " - " + orderItemList.get(i).getQuantity() + " nos.");
            }
            report.setOrderStatus(order.getStatus().name());
            salesReport.add(report);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("grandTotal", Math.round(dateOrders.stream().map(Order::getTotalPrice).reduce(0.0, Double::sum)*100.0)/100.0);
        parameters.put("quantity", dateOrders.stream().map(Order::getTotalQuantity).reduce(0, Integer::sum));
        parameters.put("totalOrders", dateOrders.size());


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(salesReport);
        InputStream stream = getClass().getClassLoader().getResourceAsStream("salesreport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(stream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);

        //Export to pdf
        if (format.equalsIgnoreCase("pdf")) {
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdfBytes;
        }
        //Export to xls
        if (format.equalsIgnoreCase("xlsx")) {

            JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(salesReport);
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
            JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, dataSource2);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint2);
            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream);
            exporter.exportReport();
            return outputStream.toByteArray();
        }

        
        return new byte[0];
    }


    private List<Order> filterDailyOrders(List<Order> filteredUserOrders, LocalDate today) {
        return filteredUserOrders.stream()
                .filter(order -> order.getOrderDate().isEqual(today))
                .collect(Collectors.toList());
    }

    private List<Order> filterWeeklyOrders(List<Order> filteredUserOrders, LocalDate today) {
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        return filteredUserOrders.stream()
                .filter(order -> {
                    LocalDate localDate = order.getOrderDate();
                    return localDate.isAfter(startOfWeek.minusDays(1)) && localDate.isBefore(endOfWeek.plusDays(1));
                })
                .collect(Collectors.toList());
    }

    private List<Order> filterMonthlyOrders(List<Order> filteredUserOrders, LocalDate today) {
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return filteredUserOrders.stream()
                .filter(order -> {
                    LocalDate localDate = order.getOrderDate();
                    return localDate.isAfter(startOfMonth.minusDays(1)) && localDate.isBefore(endOfMonth.plusDays(1));
                })
                .collect(Collectors.toList());
    }

    private List<Order> filterYearlyOrders(List<Order> filteredUserOrders, LocalDate today) {
        LocalDate startOfYear = today.withDayOfYear(1);
        LocalDate endOfYear = today.withDayOfYear(today.lengthOfYear());

        return filteredUserOrders.stream()
                .filter(order -> {
                    LocalDate localDate = order.getOrderDate();
                    return localDate.isAfter(startOfYear.minusDays(1)) && localDate.isBefore(endOfYear.plusDays(1));
                })
                .collect(Collectors.toList());
    }
}
