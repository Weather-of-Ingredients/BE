package com.nutritionangel.woi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.entity.MenuEntity;
import com.nutritionangel.woi.repository.DietRepository;
import com.nutritionangel.woi.repository.MenuRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openApi.MserviceKey}")
    private String serviceKey;

    @Value("${openApi.McallBackUrl}")
    private String callBackUrl;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuDTO> getMenus(String foodName) throws IOException {
        String food_name = URLEncoder.encode(foodName, StandardCharsets.UTF_8);
        String urlStr = callBackUrl + serviceKey + "/I2790/json/1/5" + "/DESC_KOR=" + food_name;
        ResponseEntity<String> response = restTemplate.getForEntity(urlStr, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.getBody());
        JsonNode rows = rootNode.path("I2790").path("row");

        List<MenuDTO> menuList = new ArrayList<>();
        for (JsonNode row : rows) {
            MenuDTO menuDTO = MenuDTO.builder()
                    .foodName(row.path("DESC_KOR").asText())
                    .carbohydrate(row.path("NUTR_CONT2").asDouble(0.0))
                    .protein(row.path("NUTR_CONT3").asDouble(0.0))
                    .fat(row.path("NUTR_CONT4").asDouble(0.0))
                    .calories(row.path("NUTR_CONT1").asDouble(0.0))
                    .build();
            menuList.add(menuDTO);
        }

        return menuList;
    }

    public void saveMenu(MenuDTO menuDTO) {
        MenuEntity menuEntity = MenuEntity.builder()
                .carbohydrate(menuDTO.getCarbohydrate())
                .protein(menuDTO.getProtein())
                .fat(menuDTO.getFat())
                .foodName(menuDTO.getFoodName())
                .calories(menuDTO.getCalories())
                .build();
        menuRepository.save(menuEntity);
    }

//    public MenuItem parsingJson(String json){
//        MenuItem item = null;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            item = mapper.readValue(json, MenuItem.class);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return item;
//    }
//
//    public MenuDTO[] fetchAndSaveMenu(int dietId, String query) {
//        // API URL 정의
//        String urlStr = callBackUrl +
//                serviceKey +
//                "/I2790/json/1/5" +
//                "/DESC_KOR=" + query;
//        String apiUrl = urlStr;
//
//        // API 호출
//        MenuDTO[] menuDTOs = restTemplate.getForObject(apiUrl, MenuDTO[].class);
//
//        // DietEntity 조회
//        DietEntity diet = dietRepository.findById(dietId)
//                .orElseThrow(() -> new RuntimeException("Diet not found"));
//
//        // MenuEntity로 변환하여 저장
//        if (menuDTOs != null) {
//            for (MenuDTO menuDTO : menuDTOs) {
//                MenuEntity menu = MenuEntity.builder()
//                        .ingredients(menuDTO.getIngredients())
//                        .carbohydrate(menuDTO.getCarbohydrate())
//                        .protein(menuDTO.getProtein())
//                        .fat(menuDTO.getFat())
//                        .foodName(menuDTO.getFoodName())
//                        .calories(menuDTO.getCalories())
//                        .diet(diet)
//                        .build();
//                menuRepository.save(menu);
//            }
//        }
//        return menuDTOs;
//    }
}
