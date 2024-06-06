package com.ashish.MyGenuineProtein.controller;


import com.ashish.MyGenuineProtein.dto.ProductDto;
import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.Product;
import com.ashish.MyGenuineProtein.model.ProductImage;
import com.ashish.MyGenuineProtein.repository.ProductImageRepository;
import com.ashish.MyGenuineProtein.repository.ProductRepository;
import com.ashish.MyGenuineProtein.service.CategoryService;
import com.ashish.MyGenuineProtein.service.ProductImageService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    ProductImageService productImageService;



    @GetMapping("/admin/getProducts")
    public String getProducts(Model model){
        String successMessage = (String) model.getAttribute("successMessage");

        List<Product> products =productService.getAllProducts().stream().
                filter(product -> !product.isDelete()).toList();
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("products",products);
        model.addAttribute("variants",variantService.getAllVariants());
        return "product/getProducts";
    }

    @GetMapping("/admin/addProducts")
    public String addProduct(Model model){
        model.addAttribute("productDto",new ProductDto());
        model.addAttribute("categories",categoryService.getAllCategory());

        return "product/addProducts";

    }

    @PostMapping("/admin/addProducts")
    public String postProduct(@ModelAttribute("productDto") ProductDto productDto,
                              @RequestParam("productImage") List<MultipartFile >files,
                              RedirectAttributes redirectAttributes)throws IOException {
        boolean update = false;
        Product product;
        if (productDto.getId()!=null){
             product = productService.findProductById(productDto.getId()).get();
             update=true;

        }else {
            product = new Product();
        }


        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCategory(categoryService.getCategoryById(productDto.getCategoryId()).get());
        product.setDelete(false);
        product.setDescription(productDto.getDescription());

        if (!files.get(0).isEmpty()){
            if (update)
                productImageService.deleteAllByProduct(product);
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file :files) {
                String imageUUID = file.getOriginalFilename();
                Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
                Files.write(fileNameAndPath,file.getBytes());
                ProductImage productImage = new ProductImage();
                productImage.setImageName(imageUUID);
                productImage.setProduct(product);
                productImages.add(productImage);
            }
            product.setProductImages(productImages);
        }

        productService.addProducts(product);
        redirectAttributes.addFlashAttribute("successMessage", " successfully!");

        return "redirect:/admin/getProducts";


    }


    @GetMapping ("/admin/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") UUID id) {
   //implemented soft delete for products
        Optional<Product> optionalProduct = productService.findProductById(id);
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setDelete(true);
            productService.saveProduct(product);
        }
        return "redirect:/admin/getProducts"; // Redirect to the product list or another appropriate page
    }

    @GetMapping("/admin/updateProduct/{id}")
    public String updateProduct(@PathVariable("id") UUID id, Model model){
        Product product =productService.findProductById(id).get();
        ProductDto productDto =new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setCategoryId((product.getCategory().getId()));
        productDto.setDescription(product.getDescription());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("flavours", variantService.getAllVariants());

        model.addAttribute("productDto",productDto);
        return "product/addProducts";

    }


}
