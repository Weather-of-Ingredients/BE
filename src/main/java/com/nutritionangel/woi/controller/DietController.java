package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.DietResponseDTO;
import com.nutritionangel.woi.dto.diet.MenuDTO;
import com.nutritionangel.woi.dto.diet.MenuResponseDTO;
import com.nutritionangel.woi.entity.DietEntity;
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
import java.util.List;
import java.util.stream.Collectors;

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
    private final MenuService menuService;
    private final S3UploadService s3UploadService;

    @Autowired
    public DietController(DietService dietService, S3UploadService s3UploadService, MenuService menuService) {
        this.dietService = dietService;
        this.menuService = menuService;
        this.s3UploadService = s3UploadService;
    }

    @PostMapping("/upload") // 테스트 코드
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3UploadService.saveFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/searchMenu")
//    public ResponseEntity<List<MenuDTO>> searchMenu(@RequestParam String query) {
//        List<MenuDTO> menuList = menuService.searchMenu(query);
//        return ResponseEntity.ok(menuList);
//    }

    @PostMapping("/diet") // 식단 작성
    public ResponseEntity<DietResponseDTO> createDiet(@RequestBody DietDTO dietDTO) {
        DietResponseDTO dietResponseDTO = dietService.createDiet(dietDTO);
        return ResponseEntity.ok(dietResponseDTO);
    }

    @PutMapping("/diet/update/{dietId}")
    public ResponseEntity<DietResponseDTO> updateDiet(@PathVariable int dietId, @RequestBody DietDTO dietDTO) {
        DietEntity updatedDiet = dietService.updateDiet(dietId, dietDTO);
        DietResponseDTO dietResponseDTO = convertToResponseDTO(updatedDiet);
        return ResponseEntity.ok(dietResponseDTO);
    }

    @DeleteMapping("/diet/delete/{dietId}")
    public ResponseEntity<Void> deleteDiet(@PathVariable int dietId) {
        dietService.deleteDiet(dietId);
        return ResponseEntity.noContent().build();
    }

    private DietResponseDTO convertToResponseDTO(DietEntity dietEntity) {
        DietResponseDTO responseDTO = new DietResponseDTO();
        responseDTO.setDietId(dietEntity.getDietId());
        responseDTO.setDate(dietEntity.getDate().toString());
        responseDTO.setType(dietEntity.getType().name());
        responseDTO.setWeek(dietEntity.getWeek().name());
        responseDTO.setMenus(dietEntity.getMenus().stream().map(menuEntity -> {
            MenuResponseDTO menuResponseDTO = new MenuResponseDTO();
            menuResponseDTO.setMenuId(menuEntity.getMenuId());
            menuResponseDTO.setIngredients(menuEntity.getIngredients());
            menuResponseDTO.setCarbohydrate(menuEntity.getCarbohydrate());
            menuResponseDTO.setProtein(menuEntity.getProtein());
            menuResponseDTO.setFat(menuEntity.getFat());
            menuResponseDTO.setFoodName(menuEntity.getFoodName());
            menuResponseDTO.setCalories(menuEntity.getCalories());
            return menuResponseDTO;
        }).collect(Collectors.toList()));
        return responseDTO;
    }

    @GetMapping("/getFood/{food_Name}")
    public String callApi(
            @PathVariable(value = "food_Name") String food_Name
//            @PathVariable(value="ckry_Name") String ckry_Name
    ) throws IOException {
        StringBuilder result = new StringBuilder();
        String food_name = URLEncoder.encode(food_Name, StandardCharsets.UTF_8);
        String urlStr = callBackUrl +
                "serviceKey=" + serviceKey +
                "&service_Type=" + service_Type +
                "&Page_No=1" +
                "&Page_Size=20" +
                "&food_Name=" + food_name;
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
