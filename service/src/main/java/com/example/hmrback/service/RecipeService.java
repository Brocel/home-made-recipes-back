package com.example.hmrback.service;

import com.example.hmrback.mapper.RecipeMapper;
import com.example.hmrback.model.Recipe;
import com.example.hmrback.model.filter.RecipeFilter;
import com.example.hmrback.persistence.entity.RecipeEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.repository.RecipeRepository;
import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.predicate.factory.RecipePredicateFactory;
import com.example.hmrback.utils.RecipeUtils;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static com.example.hmrback.exception.util.ExceptionMessageConstants.RECIPE_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.example.hmrback.exception.util.ExceptionMessageConstants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeService.class);

    // Repo
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    // Mapper
    private final RecipeMapper recipeMapper;

    /**
     * Create a new recipe.
     *
     * @param recipe   The recipe to create.
     * @param username The username of the user creating the recipe.
     * @return The created recipe.
     * @throws EntityNotFoundException if the user is not found.
     */
    public Recipe createRecipe(Recipe recipe,
                               String username) {
        UserEntity author = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        USER_NOT_FOUND_MESSAGE.formatted(username)));

        LOG.info("Création d'une recette par l'utilisateur {}",
                 username);

        RecipeEntity recipeEntity = recipeMapper.toEntity(recipe);
        recipeEntity.setAuthor(author);

        return recipeMapper.toModel(recipeRepository.save(recipeEntity));
    }

    /**
     * Search for recipes based on the provided filters.
     *
     * @param filter   The filters to apply to the search.
     * @param pageable The pagination information.
     * @return A page of recipes matching the filters.
     */
    public Page<Recipe> searchRecipes(RecipeFilter filter,
                                      Pageable pageable) {

        LOG.info("Recherche de recettes avec filtres {}",
                 filter);

        if (filter != null) {
            return recipeRepository.findAll(RecipePredicateFactory.fromFilters(filter),
                                            pageable)
                    .map(recipeMapper::toModel);
        }
        return Page.empty();
    }

    /**
     * Fetch the daily recipe based on today's date.
     * <p>
     * Algorithm:
     * Calculate a deterministic index using a hash of today's date and a fixed string
     *
     * @return The daily recipe.
     * @throws IllegalStateException if no recipes are available for today's type.
     */
    public Recipe fetchDailyRecipe() {
        LocalDate today = LocalDate.now();

        // Predicate
        RecipeFilter todayFilter = RecipeUtils.getDailyRecipeFilter(today);
        Predicate todayPredicate = RecipePredicateFactory.fromFilters(todayFilter);

        // Sort
        Sort sort = Sort.by(Sort.Order.asc("publicationDate"),
                            Sort.Order.asc("id"));

        // Recipe List
        List<RecipeEntity> recipeList = this.recipeRepository.findAll(todayPredicate,
                                                                      sort);

        if (recipeList.isEmpty()) {
            throw new IllegalStateException("No recipes for today's type");
        }

        // Index Calculus
        int seed = Objects.hash(today,
                                "DAILY_RECIPE_V1");
        int index = Math.floorMod(seed,
                                  recipeList.size());

        return recipeMapper.toModel(recipeList.get(index));
    }

    /**
     * Update an existing recipe.
     *
     * @param recipeId The ID of the recipe to update.
     * @param recipe   The updated recipe data.
     * @return The updated recipe.
     * @throws EntityNotFoundException if the recipe is not found.
     */
    @Transactional
    public Recipe updateRecipe(
            @NotNull
            Long recipeId,
            @Valid
            Recipe recipe) {

        LOG.info("Update de la recette {}",
                 recipeId);

        // Check if recipe exists
        RecipeEntity existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        RECIPE_NOT_FOUND_EXCEPTION_MESSAGE.formatted(recipeId)));

        RecipeEntity recipeEntity = recipeMapper.toEntity(recipe);
        recipeEntity.setAuthor(existingRecipe.getAuthor()); // Prevent changing the author during update

        return recipeMapper.toModel(recipeRepository.saveAndFlush(recipeEntity));
    }

    /**
     * Delete a recipe by its ID.
     *
     * @param id The ID of the recipe to delete.
     * @throws EntityNotFoundException if the recipe is not found.
     */
    @Transactional
    public void deleteRecipe(
            @NotNull
            Long id) {

        LOG.info("Suppression de la recette {}",
                 id);

        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(id);
        recipeEntity.ifPresentOrElse(recipeRepository::delete,
                                     () -> {
                                         throw new EntityNotFoundException(RECIPE_NOT_FOUND_EXCEPTION_MESSAGE.formatted(id));
                                     });
    }
}
