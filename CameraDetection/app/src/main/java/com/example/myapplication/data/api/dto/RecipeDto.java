// data/api/dto/RecipeDto.java
package com.example.myapplication.data.api.dto;
import java.util.List;


/** Spring Boot /maindishes 응답 원소의 스키마와 동일 */
public class RecipeDto {
    public int id;
    public String name; // 요리 이름 (ex. "육전")
    public List<IngredientDto> ingredients; // 각 재료{id,name}
}
