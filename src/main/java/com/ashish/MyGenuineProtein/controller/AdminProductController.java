package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.dto.ProductDto;
import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.service.CategoryService;
import com.ashish.MyGenuineProtein.service.VariantService;
import com.ashish.MyGenuineProtein.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class AdminProductController {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    VariantService variantService;



    @GetMapping("/admin/getProducts")
    public String getProducts(Model model, Pageable pageable){
        String successMessage = (String) model.getAttribute("successMessage");
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("products",productService.getAllProducts(pageable));
        model.addAttribute("variants",variantService.getAllVariants());
        return "/product/getProducts";
    }

    @GetMapping("/admin/addProducts")
    public String addProduct(Model model){
        model.addAttribute("productDto",new ProductDto());
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("flavours", variantService.getAllVariants());

        return "/product/addProducts";

    }

    @PostMapping("/admin/addProducts")
    public String postProduct(@ModelAttribute("productDto") ProductDto productDto,
                              @RequestParam("productImage")MultipartFile file,
                              @RequestParam("imgName")String imgName,
                              RedirectAttributes redirectAttributes)throws IOException {

        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCategory(categoryService.getCategoryById(productDto.getCategoryId()).get());

//        product.setVariant(variantService.getFlavourById(productDto.getFlavourId()).get());
//        product.setWeight(weightService.getweightById(productDto.getWeightId()).get());
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
        redirectAttributes.addFlashAttribute("successMessage", " successfully!");

        return "redirect:/admin/getProducts";


    }


    @GetMapping ("/admin/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
        return "redirect:/admin/getProducts"; // Redirect to the product list or another appropriate page
    }

    @GetMapping("/admin/updateProduct/{id}")
    public String updateProduct(@PathVariable("id") UUID id, Model model){
        Product product =productService.findProductById(id).get();
        ProductDto productDto =new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setCategoryId((product.getCategory().getId()));
//        productDto.setFlavourId((product.getVariant().getId()));
//        productDto.setWeightId((product.getWeight().getId()));
//        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImageName(product.getImageName());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("flavours", variantService.getAllVariants());

        model.addAttribute("productDto",productDto);
        return "/product/addProducts";

    }


}
