package com.example.hmrback.service;

import com.example.hmrback.mapper.ProductMapper;
import com.example.hmrback.model.Product;
import com.example.hmrback.model.filter.ProductFilter;
import com.example.hmrback.persistence.entity.ProductEntity;
import com.example.hmrback.persistence.repository.ProductRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.predicate.factory.ProductPredicateFactory;
import com.example.hmrback.utils.NormalizeUtils;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.hmrback.exception.util.ExceptionMessageConstants.PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.example.hmrback.exception.util.ExceptionMessageConstants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    // Repo
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Mapper
    private final ProductMapper productMapper;

    /**
     * Create a new product if it does not already exist (based on normalized name).
     *
     * @param product  The product to create.
     * @param username The username of the user creating the product.
     * @return The created or existing product.
     * @throws EntityNotFoundException if the user is not found.
     */
    public Product createProduct(
        @Valid
        Product product, String username) {
        userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(username)));
        LOG.info("Création d'un produit par l'utilisateur {}", username);

        String normalizedName = NormalizeUtils.normalizeText(product.name());
        boolean productAlreadyExists = productRepository.existsByNormalizedName(normalizedName);
        LOG.info("Le produit '{}' ({}) existe déjà :: {}", product.name(), normalizedName, productAlreadyExists);

        if (productAlreadyExists) {
            return productMapper.toModel(productRepository.findByNormalizedName(normalizedName));
        } else {
            return productMapper.toModel(productRepository.save(productMapper.toEntity(product)));
        }
    }

    /**
     * Search for products based on the provided filters.
     *
     * @param filter   The filters to apply to the search.
     * @param pageable The pagination information.
     * @return A page of products matching the filters.
     */
    public Page<Product> searchProducts(
        @NotNull
        ProductFilter filter, Pageable pageable) {

        LOG.info("Recherche de produits avec filtres {}", filter);

        Predicate predicate = ProductPredicateFactory.fromFilters(filter);

        if (predicate != null) {
            return productRepository.findAll(predicate, pageable).map(productMapper::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Update an existing product.
     *
     * @param productId The ID of the product to update.
     * @param product   The updated product data.
     * @return The updated product.
     * @throws EntityNotFoundException if the product is not found.
     */
    public Product updateProduct(Long productId,
        @Valid
        Product product) {

        LOG.info("Update du produit {}", productId);

        // Check if product exists
        boolean productExists = productRepository.existsById(productId);

        if (productExists) {
            ProductEntity productEntity = productMapper.toEntity(product);
            return productMapper.toModel(productRepository.saveAndFlush(productEntity));
        } else {
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE.formatted(productId));
        }
    }

    /**
     * Delete a product by its ID.
     *
     * @param productId The ID of the product to delete.
     * @throws EntityNotFoundException if the product is not found.
     */
    public void deleteProduct(
        @NotNull
        Long productId) {

        LOG.info("Suppression du produit {}", productId);

        Optional<ProductEntity> recipeEntity = productRepository.findById(productId);
        recipeEntity.ifPresentOrElse(productRepository::delete, () -> {
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE.formatted(productId));
        });
    }
}
