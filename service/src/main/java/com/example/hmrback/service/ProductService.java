package com.example.hmrback.service;

import com.example.hmrback.mapper.ProductMapper;
import com.example.hmrback.model.Product;
import com.example.hmrback.persistence.repository.ProductRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.NormalizeUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
}
