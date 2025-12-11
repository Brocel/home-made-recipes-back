package com.example.hmrback.service;

import com.example.hmrback.BaseTU;
import com.example.hmrback.mapper.ProductMapper;
import com.example.hmrback.persistence.repository.ProductRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.NormalizeUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
