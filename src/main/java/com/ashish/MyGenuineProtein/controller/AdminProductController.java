package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.dto.ProductDto;
import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.service.CategoryService;
import com.ashish.MyGenuineProtein.service.FlavourService;
import com.ashish.MyGenuineProtein.service.ProductService;
import com.ashish.MyGenuineProtein.service.WeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AdminProductController {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    FlavourService flavourService;

    @Autowired
    WeightService weightService;

    @GetMapping("/admin/getProducts")
    public String getProducts(Model model){
        model.addAttribute("products",productService.getAllProducts());
        return "/product/getProducts";
    }

    @GetMapping("/admin/addProducts")
    public String addProduct(Model model){
        model.addAttribute("productDto",new ProductDto());
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("flavours",flavourService.getAllFlavours());
        model.addAttribute("weights",weightService.getAllWeights());
        return "/product/addProducts";

    }

    @PostMapping("/admin/addProducts")
    public String postProduct(@ModelAttribute("productDto") ProductDto productDto,
                              @RequestParam("productImage")MultipartFile file,
                              @RequestParam("imgName")String imgName)throws IOException {

        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCategory(categoryService.getCategoryById(productDto.getCategoryId()).get());
        product.setPrice(productDto.getPrice());
        product.setFlavour(flavourService.getFlavourById(productDto.getFlavourId()).get());
        product.setWeight(weightService.getweightById(productDto.getWeightId()).get());
        product.setDescription(productDto.getDescription());
        String imageUUID;
        if(!file.isEmpty()){
            imageUUID=file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
            Files.write(fileNameAndPath,file.getBytes());
        }else {
            imageUUID = imgName;
        }
        product.setImageName(imageUUID);
        productService.addProducts(product);

        return "redirect:/admin/getProducts";


    }

}
