package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DietController {

    private final DietService dietService;

    @Autowired
    public DietController(DietService dietService) { this.dietService = dietService; }

    @PostMapping("/diet/add")
    public ResponseEntity<?> addDiet(DietDTO dietDTO) {
        DietDTO createdDiet = dietService.createDiet(dietDTO);
        return ResponseEntity.ok(createdDiet);
    }


}
