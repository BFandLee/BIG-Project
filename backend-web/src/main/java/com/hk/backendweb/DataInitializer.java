package com.hk.backendweb;

import com.hk.backendweb.domain.Ingredient;
import com.hk.backendweb.domain.MainDish;
import com.hk.backendweb.repository.IngredientRepository;
import com.hk.backendweb.repository.MainDishRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final IngredientRepository ingredientRepository;
    private final MainDishRepository mainDishRepository;

    public DataInitializer(IngredientRepository ingredientRepository, MainDishRepository mainDishRepository) {
        this.ingredientRepository = ingredientRepository;
        this.mainDishRepository = mainDishRepository;
    }

    @PostConstruct
    public void initData() {
        // "고기"가 없으면 저장
        Ingredient meat = ingredientRepository.findByName("고기")
                .orElseGet(() -> ingredientRepository.save(new Ingredient("고기")));

        // "계란"이 없으면 저장
        Ingredient egg = ingredientRepository.findByName("계란")
                .orElseGet(() -> ingredientRepository.save(new Ingredient("계란")));

        MainDish yukjeon = new MainDish("육전");
        yukjeon.addIngredient(meat);
        yukjeon.addIngredient(egg);

        MainDish eggBokkeumbap = new MainDish("계란 볶음밥");
        eggBokkeumbap.addIngredient(egg);

        MainDish eggJjim = new MainDish("계란찜");
        eggJjim.addIngredient(egg);

        MainDish meatBokkeum = new MainDish("고기 볶음");
        meatBokkeum.addIngredient(meat);

        MainDish galbijjim = new MainDish("갈비찜");
        galbijjim.addIngredient(meat);

        mainDishRepository.save(yukjeon);
        mainDishRepository.save(eggBokkeumbap);
        mainDishRepository.save(eggJjim);
        mainDishRepository.save(meatBokkeum);
        mainDishRepository.save(galbijjim);
    }
}