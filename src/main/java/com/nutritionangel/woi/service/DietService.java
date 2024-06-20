package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.diet.*;
import com.nutritionangel.woi.dto.user.UserInfoResponseDTO;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.entity.MenuEntity;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.enums.DietType;
import com.nutritionangel.woi.enums.Weeks;
import com.nutritionangel.woi.repository.DietRepository;
import com.nutritionangel.woi.repository.MenuRepository;
import com.nutritionangel.woi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietService {

    private final DietRepository dietRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Autowired
    public DietService(DietRepository dietRepository, MenuRepository menuRepository, UserRepository userRepository) {
        this.dietRepository = dietRepository;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
    }

    public List<DietDTO> getAllDiets() {
        List<DietEntity> dietEntities = dietRepository.findAll();
        return dietEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<DietDTO> getDietByDate(String date) {
        List<DietEntity> dietEntities = dietRepository.findByDate(LocalDate.parse(date));
        return dietEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<DietDTO> getDietsByUserId(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity userEntity = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<DietEntity> dietEntities = dietRepository.findByUserUserId(userEntity.getUserId());
        return dietEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public DietResponseDTO createDiet(DietDTO dietDTO, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity userEntity = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        DietEntity dietEntity = DietEntity.builder()
                .type(DietType.valueOf(dietDTO.getType()))
                .week(Weeks.valueOf(dietDTO.getWeek()))
                .date(LocalDate.parse(dietDTO.getDate()))
                .user(userEntity)
                .build();

        List<MenuEntity> menuEntities = dietDTO.getMenus().stream().map(menuDTO -> {
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

        dietEntity.setMenus(menuEntities);

        DietEntity savedDiet = dietRepository.save(dietEntity);
        menuRepository.saveAll(menuEntities);

        return convertToResponseDTO(savedDiet);
    }

    @Transactional
    public DietResponseDTO updateDiet(int dietId, DietDTO dietDTO, @AuthenticationPrincipal  UserDetails userDetails) {
        DietEntity existingDiet = dietRepository.findById(dietId)
                .orElseThrow(() -> new RuntimeException("Diet not found"));

        existingDiet.setType(DietType.valueOf(dietDTO.getType()));
        existingDiet.setWeek(Weeks.valueOf(dietDTO.getWeek()));
        existingDiet.setDate(LocalDate.parse(dietDTO.getDate()));

        UserEntity user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingDiet.setUser(user);

        List<MenuEntity> existingMenus = existingDiet.getMenus();
        List<MenuDTO> newMenuDTOs = dietDTO.getMenus();

        List<MenuEntity> updatedMenuEntities = new ArrayList<>();

        // 메뉴 업데이트 및 추가
        for (MenuDTO newMenuDTO : newMenuDTOs) {
            MenuEntity menuEntity = existingMenus.stream()
                    .filter(m -> m.getMenuId().equals(newMenuDTO.getMenuId()))
                    .findFirst()
                    .orElse(new MenuEntity());

            menuEntity.setIngredients(newMenuDTO.getIngredients());
            menuEntity.setCarbohydrate(newMenuDTO.getCarbohydrate());
            menuEntity.setProtein(newMenuDTO.getProtein());
            menuEntity.setFat(newMenuDTO.getFat());
            menuEntity.setFoodName(newMenuDTO.getFoodName());
            menuEntity.setCalories(newMenuDTO.getCalories());
            menuEntity.setDiet(existingDiet);

            updatedMenuEntities.add(menuEntity);
        }

        // 제거된 메뉴 삭제
        List<MenuEntity> menusToDelete = existingMenus.stream()
                .filter(m -> newMenuDTOs.stream().noneMatch(dto -> dto.getMenuId() != null && dto.getMenuId().equals(m.getMenuId())))
                .collect(Collectors.toList());

        menuRepository.deleteAll(menusToDelete);
        menuRepository.saveAll(updatedMenuEntities);

        existingDiet.setMenus(updatedMenuEntities);
        DietEntity updatedDiet = dietRepository.save(existingDiet);

        return convertToResponseDTO(updatedDiet);
    }

    @Transactional
    public void deleteDiet(int dietId) {
        DietEntity dietEntity = dietRepository.findById(dietId)
                .orElseThrow(() -> new RuntimeException("Diet not found"));
        menuRepository.deleteAll(dietEntity.getMenus());
        dietRepository.delete(dietEntity);
    }

    private DietDTO convertToDTO(DietEntity dietEntity) {
        DietDTO dietDTO = new DietDTO();
        dietDTO.setDietId(dietEntity.getDietId());
        dietDTO.setDate(dietEntity.getDate().toString());
        dietDTO.setType(dietEntity.getType().name());
        dietDTO.setWeek(dietEntity.getWeek().name());
        dietDTO.setMenus(dietEntity.getMenus().stream().map(this::convertToMenuDTO).collect(Collectors.toList()));
        dietDTO.setUserEntity(dietEntity.getUser());
        return dietDTO;
    }

    private MenuDTO convertToMenuDTO(MenuEntity menuEntity) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setMenuId(menuEntity.getMenuId());
        menuDTO.setIngredients(menuEntity.getIngredients());
        menuDTO.setCarbohydrate(menuEntity.getCarbohydrate());
        menuDTO.setProtein(menuEntity.getProtein());
        menuDTO.setFat(menuEntity.getFat());
        menuDTO.setFoodName(menuEntity.getFoodName());
        menuDTO.setCalories(menuEntity.getCalories());
        return menuDTO;
    }

    private DietResponseDTO convertToResponseDTO(DietEntity dietEntity) {
        DietResponseDTO responseDTO = new DietResponseDTO();
        responseDTO.setDietId(dietEntity.getDietId());
        responseDTO.setDate(dietEntity.getDate().toString());
        responseDTO.setType(dietEntity.getType().name());
        responseDTO.setWeek(dietEntity.getWeek().name());
        responseDTO.setMenus(dietEntity.getMenus().stream().map(this::convertToMenuResponseDTO).collect(Collectors.toList()));

        UserInfoResponseDTO userInfoResponse = new UserInfoResponseDTO();
        userInfoResponse.setUid(dietEntity.getUser().getUserId());
        userInfoResponse.setName(dietEntity.getUser().getName());
        userInfoResponse.setEmail(dietEntity.getUser().getEmail());
        userInfoResponse.setSchool(dietEntity.getUser().getSchool());
        responseDTO.setUser(userInfoResponse);  // 사용자 정보 설정

        return responseDTO;
    }

    private MenuResponseDTO convertToMenuResponseDTO(MenuEntity menuEntity) {
        MenuResponseDTO menuResponseDTO = new MenuResponseDTO();
        menuResponseDTO.setMenuId(menuEntity.getMenuId());
        menuResponseDTO.setIngredients(menuEntity.getIngredients());
        menuResponseDTO.setCarbohydrate(menuEntity.getCarbohydrate());
        menuResponseDTO.setProtein(menuEntity.getProtein());
        menuResponseDTO.setFat(menuEntity.getFat());
        menuResponseDTO.setFoodName(menuEntity.getFoodName());
        menuResponseDTO.setCalories(menuEntity.getCalories());
        return menuResponseDTO;
    }
}
