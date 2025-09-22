package com.hk.backendweb.repository;

import com.hk.backendweb.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Optional 임포트

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // 이름으로 Ingredient를 찾아 Optional<Ingredient> 형태로 반환
    Optional<Ingredient> findByName(String name);
}