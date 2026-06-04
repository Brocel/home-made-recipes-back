package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.mapper.dto.RecipeDTOMapper;
import com.example.hmrback.model.Recipe;
import com.example.hmrback.model.filter.RecipeFilter;
import com.example.hmrback.model.request.UpdateRecipeRequest;
import com.example.hmrback.model.response.RecipeResponse;
import com.example.hmrback.service.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;
import static com.example.hmrback.constant.ControllerConstants.RECIPES;

@RestController
@RequestMapping(BASE_PATH + RECIPES)
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<RecipeResponse> createRecipe(
            Authentication authentication,
            @Valid
            @RequestBody
            com.example.hmrback.model.request.CreateRecipeRequest request) {
        String username = authentication.getName();
        Recipe recipeInput = RecipeDTOMapper.toRecipe(request);
        Recipe createdRecipe = this.recipeService.createRecipe(recipeInput, username);
        RecipeResponse response = RecipeDTOMapper.toResponse(createdRecipe);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(BASE_PATH + RECIPES + "/" + response.id()))
                .body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<RecipeResponse>> searchRecipes(
            @RequestBody
            @NotNull
            RecipeFilter filter,
            @ParameterObject
            @PageableDefault(size = 20)
            Pageable pageable) {
        Page<Recipe> result = this.recipeService.searchRecipes(filter, pageable);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Page<RecipeResponse> responsePage = result.map(RecipeDTOMapper::toResponse);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.getTotalElements()))
                .body(responsePage);
    }

    @GetMapping("/daily")
    public ResponseEntity<RecipeResponse> fetchDailyRecipe() {
        Recipe result = this.recipeService.fetchDailyRecipe();
        return ResponseEntity.ok(RecipeDTOMapper.toResponse(result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @recipeSecurity.isAuthor(#id))")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            UpdateRecipeRequest request) {
        Recipe recipeInput = RecipeDTOMapper.toRecipe(request);
        Recipe updatedRecipe = this.recipeService.updateRecipe(id, recipeInput);
        return ResponseEntity.ok(RecipeDTOMapper.toResponse(updatedRecipe));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @recipeSecurity.isAuthor(#id))")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable
            Long id) {
        this.recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
