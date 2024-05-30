package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.service.DietService;
import com.nutritionangel.woi.service.MenuService;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Value("${openApi.serviceKey3}")
    private String serviceKey;

    @Value("${openApi.callBackUrl2}")
    private String callBackUrl;

    @Autowired
    private MenuService menuService;

    @PostMapping("/menu/save")
    public void fetchAndSaveMenus(@RequestParam int dietId) {
        menuService.fetchAndSaveMenu(dietId);
    }

    @GetMapping("/getNut/{food_Name}")
    public String callApi(
            @PathVariable(value = "food_Name") String food_Name
    ) throws IOException {
        StringBuilder result = new StringBuilder();
        String food_name = URLEncoder.encode(food_Name, StandardCharsets.UTF_8);
        String urlStr = callBackUrl +
                serviceKey +
                "/I2790/json/1/5" +
                "/DESC_KOR=" + food_name;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine + "\n\r");
        }
        urlConnection.disconnect();

        return result.toString();
    }

}
