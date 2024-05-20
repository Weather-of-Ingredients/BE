package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.service.DietService;
import com.nutritionangel.woi.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class DietController {

    @Value("${openApi.serviceKey}")
    private String serviceKey;

    @Value("${openApi.callBackUrl}")
    private String callBackUrl;

    @Value("${openApi.dataType}")
    private String service_Type;

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

    @GetMapping("/get")
    public String callApi(
            @RequestParam(value="food_Name") String food_Name,
            @RequestParam(value="ckry_Name") String ckry_Name
    ) throws IOException {
        StringBuilder result = new StringBuilder();
        String urlStr = callBackUrl +
                "serviceKey=" + serviceKey +
                "&service_Type=" + service_Type +
                "&Page_No=1" +
                "&Page_Size=20" +
                "&food_Name=" + food_Name +
                "&ckry_Name=" + ckry_Name;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String returnLine;

        while((returnLine = br.readLine()) != null){
            result.append(returnLine+"\n\r");
        }
        urlConnection.disconnect();

        return result.toString();
    }

}
