package com.ashish.MyGenuineProtein.controller;

import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.service.SalesReportService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

@Controller
public class SalesReportController {
    @Autowired
    SalesReportService salesReportService;

    @PostMapping("/admin/salesReport")
    public String filterSalesReport(@ModelAttribute("dateFilter") String dateFilter, Model model) {
        List<Order> modelOrders = salesReportService.filterOrders(dateFilter);

        model.addAttribute(dateFilter, dateFilter); // Add the dateFilter as a model attribute

        model.addAttribute("userOrders", modelOrders);
        model.addAttribute("orderFilter", getOrderFilterText(dateFilter));
        model.addAttribute("totalOrders", modelOrders.size());
        model.addAttribute("totalSales", modelOrders.stream().map(Order::getTotalPrice).reduce(0.0, Double::sum));

        return "salesReport";
    }

    private String getOrderFilterText(String dateFilter) {
        switch (dateFilter) {
            case "DAILY" -> {
                return "Daily orders";
            }
            case "WEEKLY" -> {
                return "Weekly orders";
            }
            case "MONTHLY" -> {
                return "Monthly orders";
            }
            case "YEARLY" -> {
                return "Yearly orders";
            }
            case "ALL" ->{
                return "All orders";
            }
            default -> {
                return "";
            }
        }
    }


    @GetMapping("/generateReport/{format}")
    public void generateReport(@PathVariable String format, HttpServletResponse response) throws JRException, FileNotFoundException {

        byte[] reportData = salesReportService.exportPdfReport(format);

        response.setHeader("Content-Disposition", "attachment; filename=salesreport." + format);
        response.setContentType("application/" + format);

        try (OutputStream os = response.getOutputStream()) {
            os.write(reportData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/downloadReportDate")
    public void downloadReportDate(@ModelAttribute("startDate") LocalDate startDate,
                                   @ModelAttribute("endDate") LocalDate endDate,
                                   @ModelAttribute("format") String format,
                                   HttpServletResponse response) throws JRException, FileNotFoundException {

        byte[] reportData = salesReportService.exportPdfReportForDate(startDate, endDate, format);

        response.setHeader("Content-Disposition", "attachment; filename=salesreport." + format);
        response.setContentType("application/" + format);

        try (OutputStream os = response.getOutputStream()) {
            os.write(reportData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
