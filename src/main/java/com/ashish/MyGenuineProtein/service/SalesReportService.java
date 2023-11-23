package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Order;
import net.sf.jasperreports.engine.JRException;

import java.time.LocalDate;
import java.util.List;

public interface SalesReportService {
    List<Order> filterOrders(String dateFilter);

    byte[] exportPdfReport(String format) throws JRException;

    byte[] exportPdfReportForDate(LocalDate startDate, LocalDate endDate, String format) throws JRException;
}
