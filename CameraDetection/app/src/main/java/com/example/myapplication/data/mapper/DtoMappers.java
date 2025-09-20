package com.example.myapplication.data.mapper;

// data/mapper/DtoMappers.java


import com.example.myapplication.data.api.RecipeDto;
import com.example.myapplication.domain.model.Recipe;
import java.util.stream.Collectors;

public final class DtoMappers {
    private DtoMappers() {}

    public static Recipe toDomain(RecipeDto dto) {
        return new Recipe(dto.id, dto.title, dto.steps, dto.usedIngredients);
    }
}
