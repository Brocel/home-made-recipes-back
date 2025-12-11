package com.example.hmrback.api.controller.product;

import com.example.hmrback.model.Product;
import com.example.hmrback.model.filter.ProductFilter;
import com.example.hmrback.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
        @RequestBody
        @NotNull
        ProductFilter filter, Pageable pageable) {
        Page<Product> result = this.productService.searchProducts(filter, pageable);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.getTotalElements())).body(result.getContent());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(
        @PathVariable
        Long id,
        @Valid
        @RequestBody
        Product product) {
        return ResponseEntity.ok(this.productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable
        Long id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
