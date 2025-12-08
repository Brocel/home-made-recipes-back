package com.example.hmrback.api.controller;

import com.example.hmrback.auth.service.JwtService;
import com.example.hmrback.persistence.entity.*;
import com.example.hmrback.persistence.repository.*;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static com.example.hmrback.utils.test.TestConstants.SHOULD_BE_INITIALIZED_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("localtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecipeBaseIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeBaseIntegrationTest.class);

    @Autowired
    public MockMvc mockMvc;

    @MockitoBean
    public RoleRepository roleRepository;
    @MockitoBean
    public UserRepository userRepository;
    @MockitoBean
    public RecipeRepository recipeRepository;
    @MockitoBean
    public StepRepository stepRepository;
    @MockitoBean
    public ProductRepository productRepository;
    @MockitoBean
    public IngredientRepository ingredientRepository;

    public static RoleEntity roleUser;
    public static RoleEntity roleAdmin;

    public static UserEntity savedUser;
    public static UserEntity savedAdmin;
    public static UserEntity savedOtherUser;

    public static String adminToken;
    public static String userToken;
    public static String otherToken;

    public static RecipeEntity savedRecipe;
    public static RecipeEntity savedOtherRecipe;

    public static List<ProductEntity> savedProducts;

    /**
     * Base setup for Integration tests
     *
     * @param context the application context
     */
    @BeforeAll
    static void setup(
        @Autowired
        ApplicationContext context) {
        LOG.info("[Integration Tests] Base Setup");

        // Role setup
        roleSetup();

        // User setup
        userSetup(context);

        // Token setup
        tokenSetup(context);

        // Recipe setup
        recipeSetup();

        // Step setup
        stepSetup();

        // Product setup
        productSetup();

        // Ingredient setup
        ingredientSetup();
    }

    @Test
    @Order(0)
    void contextLoads() {
        assertNotNull(roleUser, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Role USER"));
        assertNotNull(roleAdmin, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Role ADMIN"));

        assertNotNull(savedUser, SHOULD_BE_INITIALIZED_MESSAGE.formatted("User"));
        assertNotNull(savedAdmin, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Admin"));
        assertNotNull(savedOtherUser, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Other User"));

        assertNotNull(adminToken, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Admin token"));
        assertNotNull(userToken, SHOULD_BE_INITIALIZED_MESSAGE.formatted("User token"));
        assertNotNull(otherToken, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Other user token"));

        assertNotNull(savedRecipe, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Saved Recipe"));
        assertNotNull(savedOtherRecipe, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Saved other Recipe"));

        assertNotNull(savedProducts, SHOULD_BE_INITIALIZED_MESSAGE.formatted("Saved products"));
    }

    public static void roleSetup() {
        roleUser = EntityTestUtils.buildRoleEntity(false);
        roleAdmin = EntityTestUtils.buildRoleEntity(true);
    }

    public static void userSetup(ApplicationContext context) {
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        savedUser = EntityTestUtils.buildUserEntity(1L, true);
        savedUser.setPassword(passwordEncoder.encode("password"));
        savedUser.setRoles(Set.of(roleUser));

        savedAdmin = EntityTestUtils.buildUserEntity(2L, true);
        savedAdmin.setUsername("admin");
        savedAdmin.setPassword(passwordEncoder.encode("adminpass"));
        savedAdmin.setRoles(Set.of(roleAdmin, roleUser));

        savedOtherUser = EntityTestUtils.buildUserEntity(3L, true);
        savedOtherUser.setUsername("otherUser");
        savedOtherUser.setPassword(passwordEncoder.encode("password"));
        savedOtherUser.setRoles(Set.of(roleUser));
    }

    public static void tokenSetup(ApplicationContext context) {
        JwtService jwtService = context.getBean(JwtService.class);
        adminToken = jwtService.generateToken(savedAdmin);
        userToken = jwtService.generateToken(savedUser);
        otherToken = jwtService.generateToken(savedOtherUser);
    }

    public static void recipeSetup() {
        savedRecipe = EntityTestUtils.buildRecipeEntity(1L, true);
        savedRecipe.setAuthor(savedUser);

        savedOtherRecipe = EntityTestUtils.buildRecipeEntityIT();
        savedOtherRecipe.setAuthor(savedOtherUser);
    }

    public static void stepSetup() {
        List<StepEntity> stepList = EntityTestUtils.buildStepEntityList(5, true).stream()
            .peek(step -> step.setRecipe(savedRecipe))
            .toList();
        savedRecipe.setStepList(stepList);
    }

    public static void productSetup() {
        savedProducts = IntegrationTestUtils.buildProductEntityList();
    }

    public static void ingredientSetup() {
        savedRecipe.setIngredientList(IntegrationTestUtils.buildIngredientEntityList(savedRecipe, savedProducts));

    }

}
