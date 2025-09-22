package com.hk.backendweb.service;

import com.hk.backendweb.domain.MainDish;
import com.hk.backendweb.repository.MainDishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MainDishService {

    private final MainDishRepository mainDishRepository;

    public MainDishService(MainDishRepository mainDishRepository) {
        this.mainDishRepository = mainDishRepository;
    }

    // 모든 메인디쉬를 조회하는 로직
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션 (성능 향상)
    public List<MainDish> findAllDishes() {
        return mainDishRepository.findAll();
    }

    // 재료 이름으로 메인디쉬를 검색하는 로직
    @Transactional(readOnly = true)
    public List<MainDish> searchDishesByIngredients(List<String> ingredientNames) {
        // 복잡한 비즈니스 로직이 필요하다면 이곳에 추가합니다.
        // 예를 들어, 검색 결과가 없으면 예외를 발생시키는 등의 처리
        return mainDishRepository.findDistinctByIngredients_NameIn(ingredientNames);
    }
}