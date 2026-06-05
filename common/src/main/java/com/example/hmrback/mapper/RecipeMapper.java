package com.example.hmrback.mapper;

import com.example.hmrback.mapper.base.BaseMapper;
import com.example.hmrback.mapper.config.GlobalMapperConfig;
import com.example.hmrback.mapper.utils.DateMapper;
import com.example.hmrback.model.Recipe;
import com.example.hmrback.persistence.entity.RecipeEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
    config = GlobalMapperConfig.class,
    uses = {
        IngredientMapper.class,
        StepMapper.class,
        UserMapper.class,
        DateMapper.class
    })
public interface RecipeMapper extends BaseMapper<Recipe, RecipeEntity> {

    @Override
    @Mapping(source = "publicationDate", target = "publicationDate", qualifiedByName = "localDateToString")
    Recipe toModel(RecipeEntity entity);

    @Override
    @Mapping(source = "publicationDate", target = "publicationDate", qualifiedByName = "stringToLocalDate")
    RecipeEntity toEntity(Recipe model);

    @Override
    @Mapping(source = "publicationDate", target = "publicationDate", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateEntityFromModel(Recipe model,
        @MappingTarget
        RecipeEntity entity);

    List<Recipe> toModelList(List<RecipeEntity> entities);

    @AfterMapping
    default void linkChildren(
        @MappingTarget
        RecipeEntity recipeEntity) {
        if (recipeEntity.getIngredientList() != null) {
            recipeEntity.getIngredientList().forEach(i -> i.setRecipe(recipeEntity));
        }
        if (recipeEntity.getStepList() != null) {
            recipeEntity.getStepList().forEach(s -> s.setRecipe(recipeEntity));
        }
    }
}
