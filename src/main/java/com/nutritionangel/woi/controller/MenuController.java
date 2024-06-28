package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menus/{food_Name}")
    public ResponseEntity<List<MenuDTO>> getMenuList(@PathVariable("food_Name") String foodName) throws IOException {
        List<MenuDTO> menuList = menuService.getMenus(foodName);
        return ResponseEntity.ok(menuList);
    }

//    @PostMapping("/saveMenu")
//    public ResponseEntity<String> saveMenu(@RequestBody MenuDTO menuDTO) {
//        menuService.saveMenu(menuDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Menu saved successfully!");
//    }


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
//
//        BufferedReader br;
//
//        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
//
//        String returnLine;
//
//        while ((returnLine = br.readLine()) != null) {
//            result.append(returnLine + "\n\r");
//        }
//        urlConnection.disconnect();
//
//        return result.toString();
//    }

//    @GetMapping("/getNut/{food_Name}")
//    public MenuItem callApi(@PathVariable("food_Name") String foodName) throws IOException {
//        StringBuilder result = new StringBuilder();
//        String encodedFoodName = URLEncoder.encode(foodName, StandardCharsets.UTF_8);
//        String urlStr = callBackUrl + serviceKey + "/I2790/json/1/5/DESC_KOR=" + encodedFoodName;
//        URL url = new URL(urlStr);
//
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setRequestMethod("GET");
//        urlConnection.setRequestProperty("Content-type", "application/json");
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//        String line;
//        while ((line = br.readLine()) != null) {
//            result.append(line);
//        }
//        br.close();
//        urlConnection.disconnect();
//
//        ObjectMapper mapper = new ObjectMapper();
//        MenuItem menuItem = mapper.readValue(result.toString(), MenuItem.class);
//
//        return menuItemService.saveMenuItem(menuItem);
//    }

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
