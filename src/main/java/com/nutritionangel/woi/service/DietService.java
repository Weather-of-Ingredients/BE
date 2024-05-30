package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.entity.MenuEntity;
import com.nutritionangel.woi.enums.DietType;
import com.nutritionangel.woi.enums.Weeks;
import com.nutritionangel.woi.repository.DietRepository;
import com.nutritionangel.woi.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietService {

    private final DietRepository dietRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public DietService(DietRepository dietRepository, MenuRepository menuRepository) {
        this.dietRepository = dietRepository;
        this.menuRepository = menuRepository;
    }

    // Diet 생성 후 저장
    @Transactional
    public DietEntity createDiet(DietDTO dietDTO) {
        DietEntity dietEntity = DietEntity.builder()
                .type(DietType.valueOf(dietDTO.getType()))
                .week(Weeks.valueOf(dietDTO.getWeek()))
                .date(LocalDateTime.now())
                .build();

        DietEntity savedDiet = dietRepository.save(dietEntity);

        List<MenuEntity> menuEntities = dietDTO.getMenus().stream().map(menuDTO -> {
            return MenuEntity.builder()
                    .ingredients(menuDTO.getIngredients())
                    .carbohydrate(menuDTO.getCarbohydrate())
                    .protein(menuDTO.getProtein())
                    .fat(menuDTO.getFat())
                    .food_name(menuDTO.getFoodName())
                    .calories(menuDTO.getCalories())
                    .diet(savedDiet)
                    .build();
        }).collect(Collectors.toList());

        menuRepository.saveAll(menuEntities);

        savedDiet.setMenus(menuEntities);
        return savedDiet;
    }

    @Transactional
    public DietEntity updateDiet(int dietId, DietDTO dietDTO) {
        DietEntity existingDiet = dietRepository.findById(dietId)
                .orElseThrow(() -> new RuntimeException("Diet not found"));

        existingDiet.setType(DietType.valueOf(dietDTO.getType()));
        existingDiet.setDate(LocalDateTime.now());

        List<MenuEntity> menuEntities = dietDTO.getMenus().stream().map(menuDTO -> {
            return MenuEntity.builder()
                    .ingredients(menuDTO.getIngredients())
                    .carbohydrate(menuDTO.getCarbohydrate())
                    .protein(menuDTO.getProtein())
                    .fat(menuDTO.getFat())
                    .food_name(menuDTO.getFoodName())
                    .calories(menuDTO.getCalories())
                    .diet(existingDiet)
                    .build();
        }).collect(Collectors.toList());

        menuRepository.deleteAll(existingDiet.getMenus());
        menuRepository.saveAll(menuEntities);

        existingDiet.setMenus(menuEntities);
        return dietRepository.save(existingDiet);
    }

    @Transactional
    public void deleteDiet(int dietId) {
        DietEntity dietEntity = dietRepository.findById(dietId)
                .orElseThrow(() -> new RuntimeException("Diet not found"));
        menuRepository.deleteAll(dietEntity.getMenus());
        dietRepository.delete(dietEntity);
    }
}
