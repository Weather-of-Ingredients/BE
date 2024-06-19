package com.nutritionangel.woi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.dto.diet.MenuItem;
import com.nutritionangel.woi.entity.MenuEntity;
import com.nutritionangel.woi.service.DietService;
import com.nutritionangel.woi.service.MenuItemService;
import com.nutritionangel.woi.service.MenuService;
import com.nutritionangel.woi.service.S3UploadService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Value("${openApi.MserviceKey}")
    private String serviceKey;

    @Value("${openApi.McallBackUrl}")
    private String callBackUrl;

    @Autowired
    private MenuService menuService;

    HttpURLConnection urlConnection = null;
    InputStream stream = null;
    String result = null;
    private MenuItem menuItem;
    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/getNut/{food_Name}")
    public MenuItem callApi(@PathVariable("food_Name") String foodName) throws IOException {
        StringBuilder result = new StringBuilder();
        String encodedFoodName = URLEncoder.encode(foodName, StandardCharsets.UTF_8);
        String urlStr = callBackUrl + serviceKey + "/I2790/json/1/5/DESC_KOR=" + encodedFoodName;
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();
        urlConnection.disconnect();

        ObjectMapper mapper = new ObjectMapper();
        MenuItem menuItem = mapper.readValue(result.toString(), MenuItem.class);

        return menuItemService.saveMenuItem(menuItem);
    }

//    @GetMapping("/getNut/{food_Name}")
//    public String callApi(
//            @PathVariable(value = "food_Name") String food_Name
//    ) throws IOException {
//        StringBuilder result = new StringBuilder();
//        String food_name = URLEncoder.encode(food_Name, StandardCharsets.UTF_8);
//        String urlStr = callBackUrl +
//                serviceKey +
//                "/I2790/json/1/5" +
//                "/DESC_KOR=" + food_name;
//        URL url = new URL(urlStr);
//
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setRequestMethod("GET");
//        urlConnection.setRequestProperty("Content-type", "application/json");
//
//        menuItem = menuService.parsingJson(String.valueOf(result));
//
//        urlConnection.disconnect();
//
//        return result.toString();
//    }

}
