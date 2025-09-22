// domain/model/Recipe.java  (도메인 예시)
package com.example.myapplication.domain.model;
import java.util.List;

/** 앱 내부에서 쓰는 도메인 모델 */
public class Recipe {
    public final int id;
    public final String name;
    public final List<String> ingredients;

    public Recipe(int id, String name, List<String> ingredients) {
        this.id = id; this.name = name; this.ingredients = ingredients;
    }
}
