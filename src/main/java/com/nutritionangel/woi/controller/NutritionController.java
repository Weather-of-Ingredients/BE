package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.diet.DietResponseDTO;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.service.NutritionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;


    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }
    @GetMapping("/carbohydrate/{year}/{month}")
    public double getCarbohydrate(@PathVariable("year") int year, @PathVariable("month") int month){
        return nutritionService.getCarbohydrate(year, month);
    }

    @GetMapping("/protein/{year}/{month}")
    public double getProtein(@PathVariable("year") int year, @PathVariable("month") int month){
        return nutritionService.getProtein(year, month);
    }

    @GetMapping("/fat/{year}/{month}")
    public double getFat(@PathVariable("year") int year, @PathVariable("month") int month){
        return nutritionService.getFat(year, month);
    }
}


