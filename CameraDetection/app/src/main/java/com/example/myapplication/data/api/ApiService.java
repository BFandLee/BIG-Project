package com.example.myapplication.data.api;

import com.example.myapplication.data.api.dto.IngredientDto;
import com.example.myapplication.data.api.dto.RecipeDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/** Spring Boot Retrofit 인터페이스 */
public interface ApiService {

    /** 2) 전체 메인 요리 목록 */
    @GET("maindishes")
    Call<List<RecipeDto>> getMainDishes();

    /** (옵션) 전체 재료 */
    @GET("ingredients")
    Call<List<IngredientDto>> getIngredients();
}
