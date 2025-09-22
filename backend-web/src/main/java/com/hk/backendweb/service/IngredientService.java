package com.hk.backendweb.service;

import com.hk.backendweb.domain.Ingredient;
import com.hk.backendweb.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional
    public Ingredient addIngredient(String name) {
        // 1. 먼저 이름으로 재료가 이미 존재하는지 확인
        ingredientRepository.findByName(name).ifPresent(ingredient -> {
            // 2. 만약 존재한다면, 예외를 발생시킴
            throw new IllegalStateException("이미 존재하는 재료입니다.");
        });

        // 3. 존재하지 않는다면, 새로운 Ingredient 객체를 생성하여 저장
        Ingredient newIngredient = new Ingredient(name);
        return ingredientRepository.save(newIngredient);
    }
}