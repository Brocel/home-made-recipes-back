package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.model.Recipe;
import com.example.hmrback.model.filter.RecipeFilter;
import com.example.hmrback.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.example.hmrback.constant.ControllerConstants.BASE_PATH;
import static com.example.hmrback.constant.ControllerConstants.RECIPES;

@Tag(name = "Recipes", description = "Recipe operations")
@RestController
@RequestMapping(BASE_PATH + RECIPES)
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(
        summary = "Create a recipe",
        description = "Creates a new recipe. Requires ADMIN or USER role.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Recipe to create",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Recipe.class),
            examples = @ExampleObject(
                name = "CreateRecipeExample",
                value = """
                    {
                      "name": "Pancakes",
                      "description": "Easy homemade pancakes",
                      "servings": 4,
                      "ingredients": [
                        {
                          "quantity": 200,
                          "unit": "GRAM",
                          "product": {
                            "id": 1
                          }
                        }
                      ]
                    }
                    """
            )
        )
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Recipe> createRecipe(
        @AuthenticationPrincipal
        UserDetails userDetails,
        @Valid
        @RequestBody
        Recipe recipe) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(this.recipeService.createRecipe(recipe, username));
    }

    @Operation(
        summary = "Search recipes",
        description = "Search recipes using filters and pagination.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Search filters",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = RecipeFilter.class),
            examples = @ExampleObject(
                name = "RecipeSearchExample",
                value = """
                    {
                      "title": "pancake",
                      "description": "Pancake recipe",
                      "maximum_preparation_time": 30,
                      "recipe_type_list": ["MAIN_COURSE", "DESSERT"],
                      "author_username": "chef_john",
                      "ingredient_name_list": ["flour", "milk", "egg"],
                      "ingredient_type_list": ["DAIRY", "GRAIN"]
                    }
                    """
            )
        )
    )
    @PostMapping("/search")
    public ResponseEntity<Page<Recipe>> searchRecipes(
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

        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.getTotalElements())).body(result);
    }

    @Operation(
        summary = "Update a recipe",
        description = "Updates an existing recipe. Requires ADMIN or recipe author.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
        name = "id",
        description = "Recipe ID",
        required = true,
        example = "42"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Updated recipe data",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Recipe.class),
            examples = @ExampleObject(
                name = "UpdateRecipeExample",
                value = """
                    {
                      "name": "Updated Pancakes",
                      "description": "Even better pancakes",
                      "servings": 6
                    }
                    """
            )
        )
    )
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

    @Operation(
        summary = "Delete a recipe",
        description = "Deletes a recipe. Requires ADMIN or recipe author.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
        name = "id",
        description = "Recipe ID",
        required = true,
        example = "42"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @recipeSecurity.isAuthor(#id))")
    public ResponseEntity<Void> deleteRecipe(
        @PathVariable
        Long id) {
        this.recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
