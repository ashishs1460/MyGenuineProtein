package com.ashish.MyGenuineProtein.controller;

import ch.qos.logback.classic.spi.ConfiguratorRank;
import com.ashish.MyGenuineProtein.model.InvoiceTable;
import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.OrderItems;
import com.ashish.MyGenuineProtein.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.*;

@Controller
public class InvoiceController {
    @Autowired
    OrderService orderService;
    @GetMapping("/invoice/{id}")
    public void invoiceDownload(@PathVariable Long id ,
                                  HttpServletResponse response) throws JRException, IOException {
        Order orders = orderService.findByOrderId(id).get();

        List<InvoiceTable> invoiceTableList = new ArrayList<>();

        float totalAmount = 0f;
        float discountAmount =0f;
        for (OrderItems item: orders.getOrderItems()){
            InvoiceTable table = new InvoiceTable();
            table.setProductName(item.getVariant().getProduct().getName());
            table.setQuantity(item.getQuantity());
            table.setUnitPrice(item.getVariantPrice());
            table.setDiscount(item.getVariant().getDiscountedPrice());
            discountAmount=item.getVariant().getDiscountedPrice();
            if (item.getVariant().getDiscountedPrice()>0){
                table.setSubTotal(item.getQuantity()*item.getVariant().getDiscountedPrice());
                totalAmount = item.getQuantity()*item.getVariant().getDiscountedPrice();
            }else {
                table.setSubTotal((float) (item.getQuantity()*item.getVariantPrice()));
                totalAmount = (float) (item.getQuantity()*item.getVariantPrice());
            }

            invoiceTableList.add(table);


        }

        Map<String , Object> parameters = new HashMap<>();
        parameters.put("customerName",orders.getUser().getFirstName()+" "+orders.getUser().getLastName());
        parameters.put("customerEmail",orders.getUser().getEmail());
        parameters.put("customerAddress",orders.getAddress().getFullAddress());
        parameters.put("orderId",orders.getOrderId());
        parameters.put("orderDate",orders.getOrderDate());
        parameters.put("orderStatus",orders.getStatus().name());
        parameters.put("paymentMethod",orders.getPaymentMode().name());
        parameters.put("total",orders.getTotalPrice());

        if (totalAmount != orders.getTotalPrice() && discountAmount == 0){
            parameters.put("couponApplied", "YES");
        }else {
            parameters.put("couponApplied","NO");
        }


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoiceTableList);

        parameters.put("invoiceDataset",dataSource);

        InputStream stream = getClass().getClassLoader().getResourceAsStream("invoice.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(stream);
        JasperPrint print = JasperFillManager.fillReport(jasperReport,parameters,new JREmptyDataSource());

        byte [] pdf = JasperExportManager.exportReportToPdf(print);
         response.setHeader("Content-Disposition","attachment; filename=invoice.pdf");
         response.setContentType("application/pdf");

         response.getOutputStream().write(pdf);



    }
}
