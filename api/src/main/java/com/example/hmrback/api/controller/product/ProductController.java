package com.example.hmrback.api.controller.product;

import com.example.hmrback.model.Product;
import com.example.hmrback.model.filter.ProductFilter;
import com.example.hmrback.model.request.CreateProductRequest;
import com.example.hmrback.model.request.UpdateProductRequest;
import com.example.hmrback.model.response.ProductResponse;
import com.example.hmrback.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;
import static com.example.hmrback.constant.ControllerConstants.PRODUCTS;

@RestController
@RequestMapping(BASE_PATH + PRODUCTS)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ProductResponse> createProduct(
            Authentication authentication,
            @RequestBody
            @Valid
            CreateProductRequest request) {
        String username = authentication.getName();
        Product productInput = requestToProduct(request);
        Product createdProduct = this.productService.createProduct(productInput, username);
        ProductResponse response = productToResponse(createdProduct);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(BASE_PATH + PRODUCTS + "/" + response.id()))
                .body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestBody
            @NotNull
            ProductFilter filter,
            @ParameterObject
            @PageableDefault(size = 20)
            Pageable pageable) {
        Page<Product> result = this.productService.searchProducts(filter, pageable);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Page<ProductResponse> responsePage = result.map(this::productToResponse);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.getTotalElements()))
                .body(responsePage);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            UpdateProductRequest request) {
        Product productInput = requestToProduct(request);
        Product updatedProduct = this.productService.updateProduct(id, productInput);
        return ResponseEntity.ok(productToResponse(updatedProduct));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable
            Long id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Conversion helpers
    private Product requestToProduct(CreateProductRequest request) {
        return new Product(null, request.name(), request.ingredientType());
    }

    private Product requestToProduct(UpdateProductRequest request) {
        return new Product(null, request.name(), request.ingredientType());
    }

    private ProductResponse productToResponse(Product product) {
        return new ProductResponse(product.id(), product.name(), product.ingredientType());
    }
}
