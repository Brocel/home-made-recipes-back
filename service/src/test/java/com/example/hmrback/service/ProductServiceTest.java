package com.example.hmrback.service;

import com.example.hmrback.BaseTU;
import com.example.hmrback.mapper.ProductMapper;
import com.example.hmrback.model.Product;
import com.example.hmrback.model.filter.ProductFilter;
import com.example.hmrback.persistence.entity.ProductEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.repository.ProductRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.NormalizeUtils;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.example.hmrback.utils.test.ProductFilterEnum;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.hmrback.exception.util.ExceptionMessageConstants.PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.example.hmrback.exception.util.ExceptionMessageConstants.USER_NOT_FOUND_MESSAGE;
import static com.example.hmrback.utils.test.TestConstants.NOT_NULL_MESSAGE;
import static com.example.hmrback.utils.test.TestConstants.NUMBER_1;
import static com.example.hmrback.utils.test.TestConstants.SHOULD_BE_EQUALS_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest extends BaseTU {

    @InjectMocks
    private ProductService service;

    // Repo
    @Mock
    private ProductRepository repository;
    @Mock
    private UserRepository userRepository;

    // Mapper
    @Mock
    private ProductMapper productMapper;

    private static Product product;
    private static ProductEntity productEntity;
    private static UserEntity userEntity;
    private static ProductFilter productFilter;
    private static List<ProductEntity> productEntityList;

    @BeforeAll
    static void setup() {
        product = ModelTestUtils.buildProduct(NUMBER_1);
        productEntity = EntityTestUtils.buildProductEntity(NUMBER_1,
                                                           false);
        userEntity = EntityTestUtils.buildUserEntity(NUMBER_1,
                                                     false);
        productFilter = CommonTestUtils.buildProductFilter(ProductFilterEnum.JUST_NAME,
                                                           true);
        productEntityList = EntityTestUtils.buildProductEntityList(5,
                                                                   false);
    }

    @Test
    @Order(1)
    void createProduct_ProductDoesNotExist_ShouldCreateNewProduct() {
        String normalizedName = NormalizeUtils.normalizeText(product.name());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(repository.existsByNormalizedName(anyString())).thenReturn(false);
        when(productMapper.toEntity(any(Product.class))).thenReturn(productEntity);
        when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(productMapper.toModel(any(ProductEntity.class))).thenReturn(product);

        Product result = service.createProduct(product,
                                               "testuser");

        assertNotNull(result);
        assertEquals(product,
                     result,
                     "The created product should match the expected product.");

        verify(userRepository,
               times(1)).findByUsername(anyString());
        verify(repository,
               times(1)).existsByNormalizedName(normalizedName);
        verify(productMapper,
               times(1)).toEntity(product);
        verify(repository,
               times(1)).save(productEntity);
        verify(productMapper,
               times(1)).toModel(productEntity);
        verify(repository,
               times(0)).findByNormalizedName(normalizedName);
    }

    @Test
    @Order(2)
    void createProduct_ProductAlreadyExists_ShouldReturnExistingProduct() {
        String normalizedName = NormalizeUtils.normalizeText(product.name());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(repository.existsByNormalizedName(anyString())).thenReturn(true);
        when(repository.findByNormalizedName(anyString())).thenReturn(productEntity);
        when(productMapper.toModel(any(ProductEntity.class))).thenReturn(product);

        Product result = service.createProduct(product,
                                               "testuser");

        assertNotNull(result,
                      NOT_NULL_MESSAGE.formatted("Product"));
        assertEquals(product,
                     result,
                     "The created product should match the expected product.");

        verify(userRepository,
               times(1)).findByUsername(anyString());
        verify(repository,
               times(1)).existsByNormalizedName(normalizedName);
        verify(productMapper,
               times(0)).toEntity(product);
        verify(repository,
               times(0)).save(productEntity);
        verify(productMapper,
               times(1)).toModel(productEntity);
        verify(repository,
               times(1)).findByNormalizedName(normalizedName);

    }

    @Test
    @Order(3)
    void createProduct_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                                                  () -> service.createProduct(product,
                                                                              "nonexistentuser"));

        assertNotNull(ex,
                      NOT_NULL_MESSAGE.formatted("EntityNotFoundException"));
        assertEquals(USER_NOT_FOUND_MESSAGE.formatted("nonexistentuser"),
                     ex.getMessage());

        verify(userRepository,
               times(1)).findByUsername(anyString());
        verify(repository,
               times(0)).existsByNormalizedName(anyString());
        verify(productMapper,
               times(0)).toEntity(any(Product.class));
        verify(repository,
               times(0)).save(any(ProductEntity.class));
        verify(productMapper,
               times(0)).toModel(any(ProductEntity.class));
        verify(repository,
               times(0)).findByNormalizedName(anyString());
    }

    @Test
    @Order(4)
    void searchProducts_WithValidFilter_ShouldReturnProductsPage() {
        when(repository.findAll(any(Predicate.class),
                                any(Pageable.class))).thenReturn(new PageImpl<>(productEntityList));
        when(productMapper.toModel(any(ProductEntity.class))).thenReturn(product);

        Page<Product> result = service.searchProducts(productFilter,
                                                      PageRequest.of(0,
                                                                     10));

        assertNotNull(result,
                      NOT_NULL_MESSAGE.formatted("Page<Product>"));
        assertNotNull(result.getContent(),
                      NOT_NULL_MESSAGE.formatted("Page<Product>.content"));
        assertEquals(5,
                     result.getTotalElements(),
                     SHOULD_BE_EQUALS_MESSAGE.formatted("Total elements",
                                                        "5"));

        verify(repository,
               times(1)).findAll(any(Predicate.class),
                                 any(Pageable.class));
        verify(productMapper,
               times(5)).toModel(any(ProductEntity.class));
    }

    @Test
    @Order(5)
    void shouldSearchProducts_whenFiltersIsNull_thenReturnEmptyList() {
        when(repository.findAll(any(Predicate.class),
                                any(Pageable.class))).thenReturn(Page.empty());

        Page<Product> result = service.searchProducts(productFilter,
                                                      PageRequest.of(0,
                                                                     10));

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertEquals(0,
                     result.getTotalElements());

        verify(repository,
               times(1)).findAll(any(Predicate.class),
                                 any(Pageable.class));
        verify(productMapper,
               times(0)).toModel(any(ProductEntity.class));
    }

    @Test
    @Order(6)
    void updateProduct_ProductExists_ShouldUpdateAndReturnProduct() {
        when(repository.existsById(any(Long.class))).thenReturn(true);
        when(productMapper.toEntity(any(Product.class))).thenReturn(productEntity);
        when(repository.saveAndFlush(any(ProductEntity.class))).thenReturn(productEntity);
        when(productMapper.toModel(any(ProductEntity.class))).thenReturn(product);

        Product result = service.updateProduct(NUMBER_1,
                                               product);

        assertNotNull(result,
                      NOT_NULL_MESSAGE.formatted("Product"));
        assertEquals(product,
                     result,
                     "The updated product should match the expected product.");

        verify(repository,
               times(1)).existsById(NUMBER_1);
        verify(productMapper,
               times(1)).toEntity(product);
        verify(repository,
               times(1)).saveAndFlush(productEntity);
        verify(productMapper,
               times(1)).toModel(productEntity);
    }

    @Test
    @Order(7)
    void updateProduct_ProductDoesNotExist_ShouldThrowException() {
        when(repository.existsById(any(Long.class))).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                                                  () -> service.updateProduct(NUMBER_1,
                                                                              product));

        assertNotNull(ex,
                      NOT_NULL_MESSAGE.formatted("EntityNotFoundException"));
        assertEquals(PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE.formatted(NUMBER_1),
                     ex.getMessage());

        verify(repository,
               times(1)).existsById(NUMBER_1);
        verify(productMapper,
               times(0)).toEntity(any(Product.class));
        verify(repository,
               times(0)).saveAndFlush(any(ProductEntity.class));
        verify(productMapper,
               times(0)).toModel(any(ProductEntity.class));
    }

    @Test
    @Order(8)
    void deleteProduct_ProductExists_ShouldDeleteProduct() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.ofNullable(productEntity));
        doNothing().when(repository)
                   .delete(any(ProductEntity.class));

        service.deleteProduct(NUMBER_1);

        verify(repository,
               times(1)).findById(NUMBER_1);
        verify(repository,
               times(1)).delete(any(ProductEntity.class));
    }

    @Test
    @Order(9)
    void deleteProduct_ProductDoesNotExist_ShouldThrowException() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                                                  () -> service.deleteProduct(1L));

        assertNotNull(ex,
                      NOT_NULL_MESSAGE.formatted("EntityNotFoundException"));
        assertEquals(PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE.formatted(NUMBER_1),
                     ex.getMessage());

        verify(repository,
               times(1)).findById(NUMBER_1);
        verify(repository,
               times(0)).delete(any(ProductEntity.class));
    }
}
