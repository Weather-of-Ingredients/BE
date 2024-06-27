package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.diet.DietResponseDTO;
import com.nutritionangel.woi.dto.diet.MenuResponseDTO;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.repository.DietRepository;
import com.nutritionangel.woi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NutritionService {

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private DietService dietService;

    @Autowired
    private UserRepository userRepository;

    public NutritionService(DietRepository dietRepository, DietService dietService, UserRepository userRepository) {
        this.dietRepository = dietRepository;
        this.dietService = dietService;
        this.userRepository = userRepository;
    }

    private List<DietResponseDTO> getDietResponseDTO(UserDetails userDetails, int year, int month){

        UserEntity userEntity = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<DietEntity> dietEntityList = dietRepository.findByYearAndMonthWithUser(userEntity.getUserId(), year, month);


        List<DietResponseDTO> dietResponseDTOList = new ArrayList<>();

        for(DietEntity dietEntity : dietEntityList){
            dietResponseDTOList.add(dietService.callToResponseDTO(dietEntity));
        }

        return dietResponseDTOList;
    }
    public double getCarbohydrate(UserDetails userDetails, int year, int month) {
        double getCarbohydrate = 0;
        List<DietResponseDTO> dietResponseDTOList = getDietResponseDTO(userDetails, year, month);

        for(DietResponseDTO dietResponseDTO : dietResponseDTOList){
            List<MenuResponseDTO> menuResponseDTOList = dietResponseDTO.getMenus();
            for(MenuResponseDTO menuResponseDTO : menuResponseDTOList){
                getCarbohydrate += menuResponseDTO.getCarbohydrate();
            }
        }
        return getCarbohydrate;
    }

    public double getProtein(UserDetails userDetails, int year, int month) {
        double getProtein = 0;
        List<DietResponseDTO> dietResponseDTOList = getDietResponseDTO(userDetails, year, month);

        for(DietResponseDTO dietResponseDTO : dietResponseDTOList){
            List<MenuResponseDTO> menuResponseDTOList = dietResponseDTO.getMenus();
            for(MenuResponseDTO menuResponseDTO : menuResponseDTOList){
                getProtein += menuResponseDTO.getProtein();
            }
        }

        return getProtein;
    }

    public double getFat(UserDetails userDetails, int year, int month) {
        double getFat =0;
        List<DietResponseDTO> dietResponseDTOList = getDietResponseDTO(userDetails, year, month);

        for(DietResponseDTO dietResponseDTO : dietResponseDTOList){
            List<MenuResponseDTO> menuResponseDTOList = dietResponseDTO.getMenus();
            for(MenuResponseDTO menuResponseDTO : menuResponseDTOList){
                getFat += menuResponseDTO.getFat();
            }
        }

        return getFat;
    }

}
