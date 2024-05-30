package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.entity.MenuEntity;
import com.nutritionangel.woi.repository.DietRepository;
import com.nutritionangel.woi.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openApi.serviceKey3}")
    private String serviceKey;

    @Value("${openApi.callBackUrl2}")
    private String callBackUrl;

    public void fetchAndSaveMenu(int dietId) {
        // API URL 정의
        String urlStr = callBackUrl +
                serviceKey +
                "/I2790/json/1/5" +
                "/DESC_KOR=" + "울외";
//                "/DESC_KOR=" + food_name;
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
                        .ingredients(menuDTO.getIngredients())
                        .carbohydrate(menuDTO.getCarbohydrate())
                        .protein(menuDTO.getProtein())
                        .fat(menuDTO.getFat())
                        .food_name(menuDTO.getFoodName())
                        .calories(menuDTO.getCalories())
                        .diet(diet)
                        .build();
                menuRepository.save(menu);
            }
        }
    }


}
