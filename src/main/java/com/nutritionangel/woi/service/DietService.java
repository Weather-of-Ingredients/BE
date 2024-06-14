package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.DietResponseDTO;
import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.dto.diet.MenuResponseDTO;
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
import java.util.ArrayList;
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
//    @Transactional
//    public DietEntity createDiet(DietDTO dietDTO) {
//        DietEntity dietEntity = DietEntity.builder()
//                .type(DietType.valueOf(dietDTO.getType()))
//                .week(Weeks.valueOf(dietDTO.getWeek()))
//                .date(LocalDateTime.now())
//                .build();
//
//        DietEntity savedDiet = dietRepository.save(dietEntity);
//
//        return savedDiet;
//    }
    @Transactional
    public DietResponseDTO createDiet(DietDTO dietDTO) {
        DietEntity dietEntity = DietEntity.builder()
                .type(DietType.valueOf(dietDTO.getType()))
                .week(Weeks.valueOf(dietDTO.getWeek()))
                .date(LocalDateTime.now())
                .build();

        List<MenuDTO> menuDTOs = dietDTO.getMenus() != null ? dietDTO.getMenus() : new ArrayList<>();

        List<MenuEntity> menuEntities = menuDTOs.stream().map(menuDTO -> {
            MenuEntity menuEntity = MenuEntity.builder()
                    .ingredients(menuDTO.getIngredients())
                    .carbohydrate(menuDTO.getCarbohydrate())
                    .protein(menuDTO.getProtein())
                    .fat(menuDTO.getFat())
                    .foodName(menuDTO.getFoodName())
                    .calories(menuDTO.getCalories())
                    .diet(dietEntity)
                    .build();
            return menuEntity;
        }).collect(Collectors.toList());

        dietEntity.getMenus().addAll(menuEntities);

        DietEntity savedDiet = dietRepository.save(dietEntity);
        menuRepository.saveAll(menuEntities);

        return convertToResponseDTO(savedDiet);
    }

    private DietResponseDTO convertToResponseDTO(DietEntity dietEntity) {
        DietResponseDTO responseDTO = new DietResponseDTO();
        responseDTO.setDietId(dietEntity.getDietId());
        responseDTO.setDate(dietEntity.getDate().toString());
        responseDTO.setType(dietEntity.getType().name());
        responseDTO.setWeek(dietEntity.getWeek().name());
        responseDTO.setMenus(dietEntity.getMenus().stream().map(menuEntity -> {
            MenuResponseDTO menuResponseDTO = new MenuResponseDTO();
            menuResponseDTO.setMenuId(menuEntity.getMenuId());
            menuResponseDTO.setIngredients(menuEntity.getIngredients());
            menuResponseDTO.setCarbohydrate(menuEntity.getCarbohydrate());
            menuResponseDTO.setProtein(menuEntity.getProtein());
            menuResponseDTO.setFat(menuEntity.getFat());
            menuResponseDTO.setFoodName(menuEntity.getFoodName());
            menuResponseDTO.setCalories(menuEntity.getCalories());
            return menuResponseDTO;
        }).collect(Collectors.toList()));
        return responseDTO;
    }

    @Transactional
    public DietEntity updateDiet(int dietId, DietDTO dietDTO) {
        DietEntity existingDiet = dietRepository.findById(dietId)
                .orElseThrow(() -> new RuntimeException("Diet not found"));

        existingDiet.setType(DietType.valueOf(dietDTO.getType()));
        existingDiet.setWeek(Weeks.valueOf(dietDTO.getWeek()));
        existingDiet.setDate(LocalDateTime.now());

        List<MenuEntity> existingMenus = existingDiet.getMenus();
        List<MenuDTO> newMenuDTOs = dietDTO.getMenus();

        List<MenuEntity> menuEntities = new ArrayList<>();

        // 메뉴 업데이트 및 추가
        for (MenuDTO newMenuDTO : newMenuDTOs) {
            MenuEntity menuEntity;
            if (newMenuDTO.getMenuId() != null) {
                menuEntity = existingMenus.stream()
                        .filter(m -> m.getMenuId().equals(newMenuDTO.getMenuId()))
                        .findFirst()
                        .orElse(new MenuEntity());
            } else {
                menuEntity = new MenuEntity();
            }

            menuEntity.setIngredients(newMenuDTO.getIngredients());
            menuEntity.setCarbohydrate(newMenuDTO.getCarbohydrate());
            menuEntity.setProtein(newMenuDTO.getProtein());
            menuEntity.setFat(newMenuDTO.getFat());
            menuEntity.setFoodName(newMenuDTO.getFoodName());
            menuEntity.setCalories(newMenuDTO.getCalories());
            menuEntity.setDiet(existingDiet);

            menuEntities.add(menuEntity);
        }

        // 제거된 메뉴 삭제
        List<MenuEntity> menusToDelete = existingMenus.stream()
                .filter(m -> newMenuDTOs.stream().noneMatch(dto -> dto.getMenuId() != null && dto.getMenuId().equals(m.getMenuId())))
                .collect(Collectors.toList());

        menuRepository.deleteAll(menusToDelete);

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
