package com.example.hmrback.utils;

import com.example.hmrback.model.filter.RecipeFilter;
import com.example.hmrback.persistence.enums.RecipeType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RecipeUtils {

    private static final Map<DayOfWeek, RecipeType> TYPE_BY_DAY = Map.of(
            DayOfWeek.MONDAY,
            RecipeType.STARTER,
            DayOfWeek.TUESDAY,
            RecipeType.MAIN_COURSE,
            DayOfWeek.WEDNESDAY,
            RecipeType.DESSERT,
            DayOfWeek.THURSDAY,
            RecipeType.BEVERAGE,
            DayOfWeek.FRIDAY,
            RecipeType.SNACK,
            DayOfWeek.SATURDAY,
            RecipeType.APPETIZER,
            DayOfWeek.SUNDAY,
            RecipeType.SIDE_DISH);


    private RecipeUtils() {
    }

    public static RecipeType getTypeByDay(LocalDate day) {
        DayOfWeek dow = day.getDayOfWeek();
        return TYPE_BY_DAY.get(dow);
    }

    public static RecipeFilter getDailyRecipeFilter(LocalDate day) {
        return new RecipeFilter(
                null,
                null,
                null,
                List.of(getTypeByDay(day)),
                null,
                null,
                null
        );
    }
}
