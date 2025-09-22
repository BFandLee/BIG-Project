package com.example.myapplication.ui.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.domain.model.Recipe;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClick {
    private RecyclerView rv;
    private TextView tvCount;
    private RecipeAdapter adapter;
    private ArrayList<Recipe> data = new ArrayList<>();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        tvCount = findViewById(R.id.tvCount);
        rv = findViewById(R.id.rvRecipes);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecipeAdapter(this);
        rv.setAdapter(adapter);

        String json = getIntent().getStringExtra("recipes_json");
        if (json == null) {
            Toast.makeText(this, "레시피 데이터가 없습니다", Toast.LENGTH_SHORT).show();
            finish(); return;
        }
        // JSON → List<Recipe>
        try {
            Moshi moshi = new Moshi.Builder().build();
            java.lang.reflect.Type listType = Types.newParameterizedType(List.class, Recipe.class);
            List<Recipe> list = (List<Recipe>) moshi.adapter(listType).fromJson(json);
            if (list != null) data.addAll(list);
        } catch (Exception e) {
            Toast.makeText(this, "파싱 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        tvCount.setText("가능 요리 " + data.size() + "개");
        adapter.submit(data);
    }

    @Override public void onRecipeClick(Recipe r) {
        // 상세 화면으로 이동 (Recipe 한 개를 JSON으로 넘김)
        try {
            Moshi moshi = new Moshi.Builder().build();
            String one = moshi.adapter(Recipe.class).toJson(r);
            Intent i = new Intent(this, RecipeDetailActivity.class);
            i.putExtra("recipe_json", one);
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "상세 이동 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
