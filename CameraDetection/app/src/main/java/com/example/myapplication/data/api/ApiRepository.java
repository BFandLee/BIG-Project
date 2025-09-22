package com.example.myapplication.data.api;

import com.example.myapplication.data.api.dto.IngredientDto;
import com.example.myapplication.data.api.dto.RecipeDto;
import com.example.myapplication.di.NetworkModule;

import java.util.List;

import retrofit2.Call;

/** Spring Boot 호출 레포지토리 */
public class ApiRepository {

    private final ApiService service = NetworkModule.api().create(ApiService.class);

    public Call<List<RecipeDto>> getMainDishes() {
        return service.getMainDishes();
    }

    public Call<List<IngredientDto>> getIngredients() {
        return service.getIngredients();
    }
}