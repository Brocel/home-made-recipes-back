package com.example.hmrback.service;

import com.example.hmrback.exception.CustomEntityNotFoundException;
import com.example.hmrback.exception.util.ExceptionMessageEnum;
import com.example.hmrback.mapper.ProductMapper;
import com.example.hmrback.model.Product;
import com.example.hmrback.model.filter.ProductFilter;
import com.example.hmrback.persistence.entity.ProductEntity;
import com.example.hmrback.persistence.repository.ProductRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.predicate.factory.ProductPredicateFactory;
import com.example.hmrback.utils.NormalizeUtils;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
     * Create a new product if it does not already exist.
     * <ul>
     *     <li>Checks if User exists</li>
     *     <li>Checks if created product already exists in DB (search from normalizedName)</li>
     *     <li>If product already exists, return product, else persists the new product and return it</li>
     * </ul>
     *
     * @param product  The product to create.
     * @param username The username of the user creating the product.
     * @return The created or existing product.
     * @throws CustomEntityNotFoundException if the user is not found.
     */
    public Product createProduct(
            @Valid
            Product product,
            String username) throws CustomEntityNotFoundException {

        userRepository.findByUsername(username)
                      .orElseThrow(() -> new CustomEntityNotFoundException(ExceptionMessageEnum.USER_NOT_FOUND,
                                                                           ExceptionMessageEnum.USER_NOT_FOUND.getMessage()
                                                                                                              .formatted(username)));

        LOG.info("New product cretaed by: {}",
                 username);

        String normalizedName = NormalizeUtils.normalizeText(product.name());
        boolean productAlreadyExists = productRepository.existsByNormalizedName(normalizedName);
        LOG.info("Product '{}' ({}) already exists :: {}",
                 product.name(),
                 normalizedName,
                 productAlreadyExists);

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
            ProductFilter filter,
            Pageable pageable) {

        LOG.info("Search products with given filters: {}",
                 filter);

        Predicate predicate = ProductPredicateFactory.fromFilters(filter);

        if (predicate != null) {
            return productRepository.findAll(predicate,
                                             pageable)
                                    .map(productMapper::toModel);
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
     * @throws CustomEntityNotFoundException if the product is not found.
     */
    public Product updateProduct(Long productId,
                                 @Valid
                                 Product product) throws CustomEntityNotFoundException {

        LOG.info("Update product {}",
                 productId);

        // Check if product exists
        boolean productExists = productRepository.existsById(productId);

        if (productExists) {
            ProductEntity productEntity = productMapper.toEntity(product);
            return productMapper.toModel(productRepository.saveAndFlush(productEntity));
        } else {
            throw new CustomEntityNotFoundException(ExceptionMessageEnum.PRODUCT_NOT_FOUND_BY_ID,
                                                    ExceptionMessageEnum.PRODUCT_NOT_FOUND_BY_ID.getMessage()
                                                                                                .formatted(productId));
        }
    }

    /**
     * Delete a product.
     *
     * @param productId The ID of the product to delete.
     * @throws CustomEntityNotFoundException if the product is not found.
     */
    public void deleteProduct(
            @NotNull
            Long productId) throws CustomEntityNotFoundException {

        LOG.info("Deleting product: {}",
                 productId);

        Optional<ProductEntity> recipeEntity = productRepository.findById(productId);
        recipeEntity.ifPresentOrElse(productRepository::delete,
                                     () -> {
                                         throw new CustomEntityNotFoundException(ExceptionMessageEnum.PRODUCT_NOT_FOUND_BY_ID,
                                                                                 ExceptionMessageEnum.PRODUCT_NOT_FOUND_BY_ID.getMessage()
                                                                                                                             .formatted(productId));
                                     });
    }
}
