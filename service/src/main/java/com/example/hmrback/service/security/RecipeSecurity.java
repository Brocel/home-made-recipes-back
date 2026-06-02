package com.example.hmrback.service.security;

import com.example.hmrback.persistence.repository.RecipeRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("recipeSecurity")
@RequiredArgsConstructor
public class RecipeSecurity {

    private final RecipeRepository recipeRepository;

    public boolean isAuthor(@NotNull Long recipeId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return recipeRepository.findById(recipeId)
            .map(recipe -> recipe.getAuthor().getUsername().equals(currentUsername))
            .orElseThrow(() -> new AccessDeniedException("Only the recipe author can modify this recipe."));
    }
}
