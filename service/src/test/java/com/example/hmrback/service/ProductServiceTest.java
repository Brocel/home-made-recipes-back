package com.example.hmrback.service;

import com.example.hmrback.BaseTU;
import com.example.hmrback.mapper.ProductMapper;
import com.example.hmrback.model.Product;
import com.example.hmrback.persistence.entity.ProductEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.repository.ProductRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.NormalizeUtils;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static com.example.hmrback.exception.util.ExceptionMessageConstants.USER_NOT_FOUND_MESSAGE;
import static com.example.hmrback.utils.test.TestConstants.NUMBER_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ProductService.class)
class ProductServiceTest extends BaseTU {

    @Autowired
    private ProductService service;

    // Repo
    @MockitoBean
    private ProductRepository productRepository;
    @MockitoBean
    private UserRepository userRepository;

    // Mapper
    @MockitoBean
    private ProductMapper productMapper;

    private static Product product;
    private static ProductEntity productEntity;
    private static UserEntity userEntity;

    @BeforeAll
    static void setup() {
        product = ModelTestUtils.buildProduct(NUMBER_1);
        productEntity = EntityTestUtils.buildProductEntity(NUMBER_1, false);
        userEntity = EntityTestUtils.buildUserEntity(NUMBER_1, false);
    }

    @Test
    @Order(1)
    void createProduct_ProductDoesNotExist_ShouldCreateNewProduct() {
        String normalizedName = NormalizeUtils.normalizeText(product.name());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(productRepository.existsByNormalizedName(anyString())).thenReturn(false);
        when(productMapper.toEntity(any(Product.class))).thenReturn(productEntity);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(productMapper.toModel(any(ProductEntity.class))).thenReturn(product);

        Product result = service.createProduct(product, "testuser");

        assertNotNull(result);
        assertEquals(product, result, "The created product should match the expected product.");

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(productRepository, times(1)).existsByNormalizedName(normalizedName);
        verify(productMapper, times(1)).toEntity(product);
        verify(productRepository, times(1)).save(productEntity);
        verify(productMapper, times(1)).toModel(productEntity);
        verify(productRepository, times(0)).findByNormalizedName(normalizedName);

    }

    @Test
    @Order(2)
    void createProduct_ProductAlreadyExists_ShouldReturnExistingProduct() {
        String normalizedName = NormalizeUtils.normalizeText(product.name());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(productRepository.existsByNormalizedName(anyString())).thenReturn(true);
        when(productRepository.findByNormalizedName(anyString())).thenReturn(productEntity);
        when(productMapper.toModel(any(ProductEntity.class))).thenReturn(product);

        Product result = service.createProduct(product, "testuser");

        assertNotNull(result, "The result should not be null.");
        assertEquals(product, result, "The created product should match the expected product.");

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(productRepository, times(1)).existsByNormalizedName(normalizedName);
        verify(productMapper, times(0)).toEntity(product);
        verify(productRepository, times(0)).save(productEntity);
        verify(productMapper, times(1)).toModel(productEntity);
        verify(productRepository, times(1)).findByNormalizedName(normalizedName);

    }

    @Test
    @Order(3)
    void createProduct_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException result = assertThrows(EntityNotFoundException.class,
            () -> service.createProduct(product, "nonexistentuser"));

        assertNotNull(result, "The result should not be null.");
        assertEquals(USER_NOT_FOUND_MESSAGE.formatted("nonexistentuser"), result.getMessage());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(productRepository, times(0)).existsByNormalizedName(anyString());
        verify(productMapper, times(0)).toEntity(any(Product.class));
        verify(productRepository, times(0)).save(any(ProductEntity.class));
        verify(productMapper, times(0)).toModel(any(ProductEntity.class));
        verify(productRepository, times(0)).findByNormalizedName(anyString());
    }
}
