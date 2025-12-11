package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.model.Recipe;
import com.example.hmrback.model.filter.RecipeFilter;
import com.example.hmrback.service.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;
import static com.example.hmrback.constant.ControllerConstants.RECIPES;

@RestController
@RequestMapping(BASE_PATH + RECIPES)
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(
        @AuthenticationPrincipal
        UserDetails userDetails,
        @Valid
        @RequestBody
        Recipe recipe) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(this.recipeService.createRecipe(recipe, username));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Recipe>> searchRecipes(
        @RequestBody
        @NotNull
        RecipeFilter filter, Pageable pageable) {
        Page<Recipe> result = this.recipeService.searchRecipes(filter, pageable);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.getTotalElements())).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @recipeSecurity.isAuthor(#id))")
    public ResponseEntity<Recipe> updateRecipe(
        @PathVariable
        Long id,
        @Valid
        @RequestBody
        Recipe recipe) {
        return ResponseEntity.ok(this.recipeService.updateRecipe(id, recipe));
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
