package com.example.hmrback.api.controller;

import com.example.hmrback.auth.service.JwtService;
import com.example.hmrback.persistence.entity.*;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.repository.*;
import com.example.hmrback.utils.test.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("localtest")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;
    private String userToken;
    private String otherToken;

    private static String updateRecipeRequest;
    private static String createRecipeRequest;

    @Autowired
    private StepRepository stepRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RoleRepository roleRepository;

    // TODO: add logs in order to debug

    @BeforeAll
    static void initAll() throws JsonProcessingException {
        updateRecipeRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRecipe(1L, false));
        createRecipeRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRecipe(1L, true));
    }

    @BeforeEach
    void setup() {
        // Create Roles
        roleRepository.saveAllAndFlush(EntityTestUtils.buildRoleEntityList());

        RoleEntity adminR = roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElse(null);
        RoleEntity userR = roleRepository.findByName(RoleEnum.ROLE_USER).orElse(null);

        // Create test users in DB (or mock them)
        UserEntity admin = EntityTestUtils.buildUserEntity(2L, true);
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setRoles(Set.of(adminR, userR));
        userRepository.save(admin);

        UserEntity user = EntityTestUtils.buildUserEntity(1L, true);
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Set.of(userR));
        UserEntity savedUser = userRepository.save(user);

        // Create a different user without author role
        UserEntity otherUser = EntityTestUtils.buildUserEntity(3L, true);
        otherUser.setUsername("otherUser");
        otherUser.setPassword(passwordEncoder.encode("password"));
        otherUser.setRoles(Set.of(userR));
        userRepository.save(otherUser);

        // Create a recipe authored by "user1"
        RecipeEntity recipeEntity = EntityTestUtils.buildRecipeEntity(1L, true);
        recipeEntity.setAuthor(savedUser);
        RecipeEntity savedRecipe = recipeRepository.saveAndFlush(recipeEntity);

        // Fill Up the H2 DB
        List<StepEntity> stepList = EntityTestUtils.buildStepEntityList(5, true).stream()
            .peek(step -> step.setRecipe(savedRecipe))
            .toList();
        stepRepository.saveAll(stepList);

        List<ProductEntity> productEntityList = EntityTestUtils.buildProductEntityList(4, true);
        List<ProductEntity> savedProducts = productRepository.saveAllAndFlush(productEntityList);

        List<IngredientEntity> ingredientEntityList = IntStream.range(0, savedProducts.size())
            .mapToObj(num -> {
                IngredientEntity ing = EntityTestUtils.buildIngredientEntity((long) (num + 1), true);
                ing.setRecipe(savedRecipe);
                ing.setProduct(savedProducts.get(num));
                return ing;
            })
            .toList();

        ingredientRepository.saveAll(ingredientEntityList);

        // Generate JWT tokens for them
        adminToken = jwtService.generateToken(admin);
        userToken = jwtService.generateToken(savedUser);
        otherToken = jwtService.generateToken(otherUser);

        // Create another recipe (for search Integration Tests)
        RecipeEntity otherRecipeEntity = EntityTestUtils.buildRecipeEntityIT();
        otherRecipeEntity.setAuthor(otherUser);
        recipeRepository.save(otherRecipeEntity);
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void updateRecipe_AsAdmin_ShouldSucceed() throws Exception {
        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRecipeRequest))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "username1")
    void updateRecipe_AsAuthor_ShouldSucceed() throws Exception {
        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRecipeRequest))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "otherUser")
    void updateRecipe_AsOtherUser_ShouldFail() throws Exception {
        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + otherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRecipeRequest))
            .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    @WithMockUser(username = "username1")
    void deleteRecipe_AsAuthor_ShouldSucceed() throws Exception {
        mockMvc.perform(delete("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "otherUser")
    void deleteRecipe_AsOtherUser_ShouldFail() throws Exception {
        mockMvc.perform(delete("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + otherToken))
            .andExpect(status().isForbidden());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "username1")
    void createRecipe() throws Exception {
        mockMvc.perform(post("/hmr/api/recipes")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRecipeRequest))
            .andExpect(status().isOk());
    }

    private final Map<RecipeFilterEnum, Integer> expectedContentSizeResults = Map.of(
        RecipeFilterEnum.TITLE, 2,
        RecipeFilterEnum.DESCRIPTION, 2,
        RecipeFilterEnum.MAXIMUM_PREPARATION_TIME, 1,
        RecipeFilterEnum.RECIPE_TYPE, 1,
        RecipeFilterEnum.AUTHOR_USERNAME, 1,
        RecipeFilterEnum.INGREDIENT_NAME, 1,
        RecipeFilterEnum.INGREDIENT_TYPE, 1
    );

    @ParameterizedTest
    @EnumSource(RecipeFilterEnum.class)
    @Order(7)
    @WithMockUser(username = "username1")
    void searchRecipes(RecipeFilterEnum filterEnum) throws Exception {
        String recipeFilters = IntegrationTestUtils.toJson(CommonTestUtils.buildRecipeFilter(filterEnum, true));
        mockMvc.perform(post("/hmr/api/recipes/search")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeFilters)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "title,asc"))
            .andExpect(status().isOk())
            .andExpect(header().exists("X-Total-Count"))
            .andExpect(jsonPath("$.content").exists())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(expectedContentSizeResults.get(filterEnum)));
    }

    @ParameterizedTest
    @EnumSource(RecipeFilterEnum.class)
    @Order(8)
    @WithMockUser(username = "username1")
    void searchRecipes_withNoMatchingFilter(RecipeFilterEnum filterEnum) throws Exception {
        String recipeFilters = IntegrationTestUtils.toJson(CommonTestUtils.buildRecipeFilter(filterEnum, false));
        mockMvc.perform(post("/hmr/api/recipes/search")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeFilters)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "title,asc"))
            .andExpect(status().isNoContent());
    }

}
