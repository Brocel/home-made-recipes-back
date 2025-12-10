package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.model.Product;
import com.example.hmrback.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;
import static com.example.hmrback.constant.ControllerConstants.PRODUCTS;

@RestController
@RequestMapping(BASE_PATH + PRODUCTS)
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
        @AuthenticationPrincipal
        UserDetails userDetails,
        @RequestBody
        @Valid
        Product product) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(this.productService.createProduct(product, username));
    }
}
