package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.service.DietService;
import com.nutritionangel.woi.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class DietController {

    private final DietService dietService;
    private final S3UploadService s3UploadService;

    @Autowired
    public DietController(DietService dietService, S3UploadService s3UploadService) { this.dietService = dietService;
        this.s3UploadService = s3UploadService;
    }

    @PostMapping("/upload") // 테스트 코드
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        try {
            String fileUrl = s3UploadService.saveFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/diet/add")
    public ResponseEntity<?> addDiet(DietDTO dietDTO) {
        DietDTO createdDiet = dietService.createDiet(dietDTO);
        return ResponseEntity.ok(createdDiet);
    }


}
