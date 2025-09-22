package com.example.myapplication.ui.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.domain.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {

    public interface OnRecipeClick { void onRecipeClick(Recipe r); }

    private final OnRecipeClick listener;
    private final List<Recipe> data = new ArrayList<>();

    public RecipeAdapter(OnRecipeClick l) { this.listener = l; }

    public void submit(List<Recipe> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vt) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_recipe, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Recipe r = data.get(pos);
        h.tvTitle.setText(r.name);
        // 재료 표시
        StringBuilder sb = new StringBuilder();
        if (r.ingredients != null) {
            for (int i=0;i<r.ingredients.size();i++) {
                sb.append(r.ingredients.get(i));
                if (i < r.ingredients.size()-1) sb.append(", ");
            }
        }
        h.tvIngs.setText(sb.toString());
        h.itemView.setOnClickListener(v -> { if (listener != null) listener.onRecipeClick(r); });
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvIngs;
        VH(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvIngs  = v.findViewById(R.id.tvIngs);
        }
    }
}
