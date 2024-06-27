package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.service.NutritionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;


    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping("/carbohydrate/{year}/{month}")
    public double getCarbohydrate(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("year") int year, @PathVariable("month") int month){
        return nutritionService.getCarbohydrate(userDetails, year, month);
    }

    @GetMapping("/protein/{year}/{month}")
    public double getProtein(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("year") int year, @PathVariable("month") int month){
        return nutritionService.getProtein(userDetails, year, month);
    }

    @GetMapping("/fat/{year}/{month}")
    public double getFat(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("year") int year, @PathVariable("month") int month){
        return nutritionService.getFat(userDetails, year, month);
    }
}


