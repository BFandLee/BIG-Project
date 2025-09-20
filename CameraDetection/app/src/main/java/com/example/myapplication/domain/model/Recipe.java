// domain/model/Recipe.java  (도메인 예시)
package com.example.myapplication.domain.model;
import java.util.List;

public class Recipe {
    public long id;
    public String title;
    public List<String> steps;
    public List<String> usedIngredients;

    public Recipe(long id, String title, List<String> steps, List<String> usedIngredients) {
        this.id = id; this.title = title; this.steps = steps; this.usedIngredients = usedIngredients;
    }
}
