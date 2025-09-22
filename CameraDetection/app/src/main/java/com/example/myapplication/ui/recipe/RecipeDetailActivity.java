package com.example.myapplication.ui.recipe;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.R;
import com.example.myapplication.domain.model.Recipe;
import com.squareup.moshi.Moshi;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvIngredients = findViewById(R.id.tvIngredients);

        String json = getIntent().getStringExtra("recipe_json");
        if (json == null) { finish(); return; }

        try {
            Moshi moshi = new Moshi.Builder().build();
            Recipe r = moshi.adapter(Recipe.class).fromJson(json);
            if (r == null) { finish(); return; }

            tvTitle.setText(r.name);

            StringBuilder sb = new StringBuilder();
            if (r.ingredients != null) {
                for (int i=0;i<r.ingredients.size();i++) {
                    sb.append("• ").append(r.ingredients.get(i)).append(i < r.ingredients.size()-1 ? "\n" : "");
                }
            }
            tvIngredients.setText(sb.toString());

        } catch (Exception e) {
            Toast.makeText(this, "상세 파싱 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
