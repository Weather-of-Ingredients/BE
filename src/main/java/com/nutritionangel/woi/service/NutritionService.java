package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.DietResponseDTO;
import com.nutritionangel.woi.dto.diet.MenuResponseDTO;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.repository.DietRepository;
import com.nutritionangel.woi.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutritionService {

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private DietService dietService;

    private double getCarbohydrate, getProtein, getFat = 0;

    public NutritionService(DietRepository dietRepository, DietService dietService) {
        this.dietRepository = dietRepository;
        this.dietService = dietService;
    }
    private List<DietResponseDTO> getDietResponseDTO(int year, int month){
        List<DietEntity> dietEntityList = dietRepository.findByYearAndMonth(year, month);
        List<DietResponseDTO> dietResponseDTOList = new ArrayList<>();

        for(DietEntity dietEntity : dietEntityList){
            dietResponseDTOList.add(dietService.callToResponseDTO(dietEntity));
        }

        return dietResponseDTOList;
    }
    public double getCarbohydrate(int year, int month) {
        getCarbohydrate = 0;
        List<DietResponseDTO> dietResponseDTOList = getDietResponseDTO(year, month);

        for(DietResponseDTO dietResponseDTO : dietResponseDTOList){
            List<MenuResponseDTO> menuResponseDTOList = dietResponseDTO.getMenus();
            for(MenuResponseDTO menuResponseDTO : menuResponseDTOList){
                getCarbohydrate += menuResponseDTO.getCarbohydrate();
            }
        }
        return getCarbohydrate;
    }

    public double getProtein(int year, int month) {
        getProtein = 0;
        List<DietResponseDTO> dietResponseDTOList = getDietResponseDTO(year, month);

        for(DietResponseDTO dietResponseDTO : dietResponseDTOList){
            List<MenuResponseDTO> menuResponseDTOList = dietResponseDTO.getMenus();
            for(MenuResponseDTO menuResponseDTO : menuResponseDTOList){
                getProtein += menuResponseDTO.getProtein();
            }
        }

        return getProtein;
    }

    public double getFat(int year, int month) {
        getFat =0;
        List<DietResponseDTO> dietResponseDTOList = getDietResponseDTO(year, month);

        for(DietResponseDTO dietResponseDTO : dietResponseDTOList){
            List<MenuResponseDTO> menuResponseDTOList = dietResponseDTO.getMenus();
            for(MenuResponseDTO menuResponseDTO : menuResponseDTOList){
                getFat += menuResponseDTO.getFat();
            }
        }

        return getFat;
    }

}
