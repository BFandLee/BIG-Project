package com.hk.backendweb.controller;

import com.hk.backendweb.domain.MainDish;
import com.hk.backendweb.service.MainDishService; // Repository가 아닌 Service를 주입받습니다.
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainDishController {

    private final MainDishService mainDishService; // Repository 대신 Service를 의존

    public MainDishController(MainDishService mainDishService) {
        this.mainDishService = mainDishService;
    }

    // 모든 메인디쉬 조회 API
    @GetMapping("/maindishes")
    public List<MainDish> getAllMainDishes() {
        // Controller는 Service의 메서드를 호출하기만 합니다.
        return mainDishService.findAllDishes();
    }

    // 재료 기반 메인디쉬 검색 API
    @GetMapping("/maindishes/search")
    public List<MainDish> searchMainDishesByIngredients(@RequestParam List<String> ingredients) {
        return mainDishService.searchDishesByIngredients(ingredients);
    }
}