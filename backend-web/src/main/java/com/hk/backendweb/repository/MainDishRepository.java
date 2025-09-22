package com.hk.backendweb.repository;

import com.hk.backendweb.domain.MainDish;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MainDishRepository extends JpaRepository<MainDish, Long> {
    List<MainDish> findDistinctByIngredients_NameIn(List<String> ingredientNames);
}