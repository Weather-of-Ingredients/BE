package com.nutritionangel.woi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritionangel.woi.dto.crops.CropItems;
import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.dto.diet.MenuItem;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.entity.MenuEntity;
import com.nutritionangel.woi.repository.DietRepository;
import com.nutritionangel.woi.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
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

    public MenuItem parsingJson(String json){
        MenuItem item = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            item = mapper.readValue(json, MenuItem.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return item;
    }

    public MenuDTO[] fetchAndSaveMenu(int dietId, String query) {
        // API URL 정의
        String urlStr = callBackUrl +
                serviceKey +
                "/I2790/json/1/5" +
                "/DESC_KOR=" + query;
        String apiUrl = urlStr;

        // API 호출
        MenuDTO[] menuDTOs = restTemplate.getForObject(apiUrl, MenuDTO[].class);

        // DietEntity 조회
        DietEntity diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new RuntimeException("Diet not found"));

        // MenuEntity로 변환하여 저장
        if (menuDTOs != null) {
            for (MenuDTO menuDTO : menuDTOs) {
                MenuEntity menu = MenuEntity.builder()
                        .carbohydrate(menuDTO.getCarbohydrate())
                        .protein(menuDTO.getProtein())
                        .fat(menuDTO.getFat())
                        .foodName(menuDTO.getFoodName())
                        .calories(menuDTO.getCalories())
                        .diet(diet)
                        .build();
                menuRepository.save(menu);
            }
        }
        return menuDTOs;
    }
}
