package com.example.hmrback.service;

import com.example.hmrback.BaseTU;
import com.example.hmrback.mapper.RecipeMapper;
import com.example.hmrback.model.Recipe;
import com.example.hmrback.model.filter.RecipeFilter;
import com.example.hmrback.persistence.entity.RecipeEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.repository.RecipeRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.example.hmrback.utils.test.RecipeFilterEnum;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static com.example.hmrback.utils.test.TestConstants.NOT_NULL_MESSAGE;
import static com.example.hmrback.utils.test.TestConstants.NUMBER_1;
import static com.example.hmrback.utils.test.TestConstants.SHOULD_BE_EQUALS_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest extends BaseTU {

    @InjectMocks
    private RecipeService service;

    // Repo
    @Mock
    private RecipeRepository repository;
    @Mock
    private UserRepository userRepository;

    // Mapper
    @Mock
    private RecipeMapper mapper;

    private static Recipe recipe;
    private static RecipeEntity recipeEntity;
    private static List<RecipeEntity> recipeEntityList;
    private static RecipeFilter recipeFilter;
    private static UserEntity user;

    @BeforeAll
    static void setUp() {
        recipe = ModelTestUtils.buildRecipe(NUMBER_1,
                                            true);
        recipeEntity = EntityTestUtils.buildRecipeEntity(NUMBER_1,
                                                         false);
        recipeEntityList = EntityTestUtils.buildRecipeEntityList(3,
                                                                 false);
        recipeFilter = CommonTestUtils.buildRecipeFilter(RecipeFilterEnum.TITLE,
                                                         true);
        user = EntityTestUtils.buildUserEntity(NUMBER_1,
                                               false);
    }

    @Test
    @Order(1)
    void shouldCreateRecipe() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(user));
        when(repository.save(any())).thenReturn(recipeEntity);
        when(mapper.toEntity(any())).thenReturn(recipeEntity);
        when(mapper.toModel(any())).thenReturn(recipe);

        Recipe result = service.createRecipe(recipe,
                                             "username1");

        assertNotNull(result,
                      NOT_NULL_MESSAGE.formatted("Recipe"));

        verify(userRepository,
               times(1)).findByUsername(anyString());
        verify(repository,
               times(1)).save(any(RecipeEntity.class));
        verify(mapper,
               times(1)).toEntity(any(Recipe.class));
        verify(mapper,
               times(1)).toModel(any(RecipeEntity.class));
    }

    @Test
    @Order(2)
    void createRecipe_whenUserNotFound_thenThrowException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                                                  () -> service.createRecipe(recipe,
                                                                             "username1"));

        assertNotNull(ex,
                      NOT_NULL_MESSAGE.formatted("EntityNotFoundException"));

        verify(userRepository,
               times(1)).findByUsername(anyString());
        verify(repository,
               times(0)).save(any(RecipeEntity.class));
        verify(mapper,
               times(0)).toEntity(any(Recipe.class));
        verify(mapper,
               times(0)).toModel(any(RecipeEntity.class));
    }

    @Test
    @Order(3)
    void shouldSearchRecipe() {
        when(repository.findAll(any(Predicate.class),
                                any(Pageable.class))).thenReturn(new PageImpl<>(recipeEntityList));
        when(mapper.toModel(any())).thenReturn(recipe);

        Page<Recipe> result = service.searchRecipes(recipeFilter,
                                                    PageRequest.of(0,
                                                                   10));

        assertNotNull(result,
                      NOT_NULL_MESSAGE.formatted("Page<Recipe>"));
        assertNotNull(result.getContent(),
                      NOT_NULL_MESSAGE.formatted("Page<Recipe>.content"));
        assertEquals(3,
                     result.getTotalElements(),
                     SHOULD_BE_EQUALS_MESSAGE.formatted("Total elements",
                                                        "3"));

        verify(repository,
               times(1)).findAll(any(Predicate.class),
                                 any(Pageable.class));
        verify(mapper,
               times(3)).toModel(any(RecipeEntity.class));
    }

    @Test
    @Order(4)
    void shouldSearchRecipe_whenFiltersIsNull_thenReturnEmptyList() {
        when(repository.findAll(any(Predicate.class),
                                any(Pageable.class))).thenReturn(Page.empty());

        Page<Recipe> result = service.searchRecipes(recipeFilter,
                                                    PageRequest.of(0,
                                                                   10));

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertEquals(0,
                     result.getTotalElements());

        verify(repository,
               times(1)).findAll(any(Predicate.class),
                                 any(Pageable.class));
        verify(mapper,
               times(0)).toModel(recipeEntity);
    }

    @Test
    @Order(5)
    void shouldUpdateRecipe() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(recipeEntity));
        when(repository.saveAndFlush(any())).thenReturn(recipeEntity);
        when(mapper.toEntity(any())).thenReturn(recipeEntity);
        when(mapper.toModel(any())).thenReturn(recipe);

        Recipe result = service.updateRecipe(NUMBER_1,
                                             recipe);

        assertNotNull(result);

        verify(repository,
               times(1)).findById(1L);
        verify(repository,
               times(1)).saveAndFlush(any(RecipeEntity.class));
        verify(mapper,
               times(1)).toEntity(any(Recipe.class));
        verify(mapper,
               times(1)).toModel(any(RecipeEntity.class));
    }

    @Test
    @Order(5)
    void shouldDeleteRecipe() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(recipeEntity));
        doNothing().when(repository)
                .delete(any(RecipeEntity.class));

        service.deleteRecipe(NUMBER_1);

        verify(repository,
               times(1)).findById(NUMBER_1);
        verify(repository,
               times(1)).delete(any(RecipeEntity.class));
    }

    @Test
    @Order(6)
    void deleteRecipe_whenRecipeNotFound_thenThrowsException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                                                  () -> service.deleteRecipe(NUMBER_1));

        assertNotNull(ex);
        assertEquals("La recette avec l'id %s est introuvable.".formatted(NUMBER_1),
                     ex.getMessage());

        verify(repository,
               times(1)).findById(NUMBER_1);
        verify(repository,
               times(0)).delete(any(RecipeEntity.class));
    }

    @Test
    @Order(7)
    void shouldFetchDailyRecipe() {
        when(repository.findAll(any(Predicate.class),
                                any(Sort.class))).thenReturn(recipeEntityList);
        when(mapper.toModel(any())).thenReturn(recipe);

        Recipe result = service.fetchDailyRecipe();

        assertNotNull(result);

        verify(repository,
               times(1)).findAll(any(Predicate.class),
                                 any(Sort.class));
        verify(mapper,
               times(1)).toModel(any(RecipeEntity.class));
    }

    @Test
    @Order(8)
    void shouldFetchDailyRecipe_whenRecipeListEmpty_thenThrowsException() {
        when(repository.findAll(any(Predicate.class),
                                any(Sort.class))).thenReturn(new ArrayList<>());

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                                                () -> service.fetchDailyRecipe());

        assertNotNull(ex);
        assertEquals("No recipes for today's type",
                     ex.getMessage());

        verify(repository,
               times(1)).findAll(any(Predicate.class),
                                 any(Sort.class));
        verify(mapper,
               times(0)).toModel(any(RecipeEntity.class));
    }

}
