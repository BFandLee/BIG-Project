package com.example.myapplication.data.mapper;

// data/mapper/DtoMappers.java

import com.example.myapplication.data.api.dto.RecipeDto;
import com.example.myapplication.domain.model.Recipe;

import java.util.*;
import java.util.stream.Collectors;

/** DTO → 도메인 변환 + 필터/정렬 유틸 */
public final class DtoMappers {
    private DtoMappers(){}

    /** RecipeDto → Recipe (도메인) */
    public static Recipe toDomain(RecipeDto dto) {
        List<String> names = new ArrayList<>();
        if (dto.ingredients != null) {
            for (com.example.myapplication.data.api.dto.IngredientDto ing : dto.ingredients) {
                if (ing != null && ing.name != null) names.add(ing.name);
            }
        }
        return new Recipe(dto.id, dto.name, names);
    }

    /** 감지 재료만 쓰는 요리만 남기고, 재료 수가 많은 순으로 정렬 */
    public static List<Recipe> filterAndRankByDetected(List<RecipeDto> all, Collection<String> detectedNames) {
        if (all == null || detectedNames == null) return Collections.emptyList();

        // 1) 감지 재료 정규화 집합
        Set<String> detected = new HashSet<>();
        for (String s : detectedNames) {
            String c = NameNormalizer.canon(s);
            if (c != null && !c.isEmpty()) detected.add(c);
        }

        List<Recipe> kept = new ArrayList<>();
        for (RecipeDto dto : all) {
            if (dto == null || dto.ingredients == null) continue;

            boolean usesOnlyDetected = true;
            for (com.example.myapplication.data.api.dto.IngredientDto ing : dto.ingredients) {
                String name = (ing != null) ? ing.name : null;
                if (name == null || !detected.contains(name)) {
                    usesOnlyDetected = false;
                    break;
                }
            }
            if (usesOnlyDetected) kept.add(toDomain(dto));
        }
        kept.sort((a, b) -> Integer.compare
                ( b.ingredients != null ? b.ingredients.size() : 0,
                  a.ingredients != null ? a.ingredients.size() : 0)
        );
        // 4) ★ 중복 제거: '요리 이름' 기준으로 첫 항목만 유지 (정렬 유지 위해 LinkedHashMap)
        Map<String, Recipe> uniqueByName = new LinkedHashMap<>();
        for (Recipe r : kept) {
            String key = normName(r.name);
            if (!uniqueByName.containsKey(key)) {
                uniqueByName.put(key, r);
            }
        }
        return new ArrayList<>(uniqueByName.values());
    }


    // 이름 정규화(공백 제거/소문자화 + 동의어 정규화)
    private static String normName(String raw) {
        String c = NameNormalizer.canon(raw);
        if (c == null) return "";
        return c.replaceAll("\\s+","").toLowerCase(Locale.ROOT);
    }
}
