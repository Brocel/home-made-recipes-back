package com.example.hmrback.api.controller.product;

import com.example.hmrback.model.Product;
import com.example.hmrback.model.filter.ProductFilter;
import com.example.hmrback.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;
import static com.example.hmrback.constant.ControllerConstants.PRODUCTS;

@Tag(name = "Products", description = "Product operations")
@RestController
@RequestMapping(BASE_PATH + PRODUCTS)
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
        summary = "Create product",
        description = "Requires ADMIN or USER",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Product.class),
            examples = @ExampleObject(value = "{\"name\":\"Organic Honey\",\"ingredient_type\":\"SUGAR\"}")
        )
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Product> createProduct(
        @AuthenticationPrincipal
        UserDetails userDetails,
        @RequestBody
        @Valid
        Product product) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(this.productService.createProduct(product, username));
    }

    @Operation(
        summary = "Search products",
        description = "Filter and pageable search",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProductFilter.class),
            examples = @ExampleObject(value = "{\"name\":\"Organic Honey\",\"ingredient_type_list\":[\"SUGAR\"]}")
        )
    )
    @PostMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
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

        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.getTotalElements())).body(result);
    }

    @Operation(
        summary = "Update product",
        description = "Requires ADMIN",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
        name = "id",
        description = "Product id",
        required = true,
        example = "123")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Product.class),
            examples = @ExampleObject(value = "{\"name\":\"Organic Honey\",\"ingredient_type\":\"SUGAR\"}")
        )
    )
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

    @Operation(
        summary = "Delete product",
        description = "Requires ADMIN",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
        name = "id",
        description = "Product id",
        required = true,
        example = "123")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable
        Long id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
