package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.DietResponseDTO;
import com.nutritionangel.woi.dto.diet.MenuResponseDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.service.DietService;
import com.nutritionangel.woi.service.MenuService;
import com.nutritionangel.woi.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/diet/all") // 식단 목록 조회
    public ResponseEntity<List<DietDTO>> getDietList(){
        List<DietDTO> dietList = dietService.getAllDiets();
        return ResponseEntity.ok(dietList);
    }

    @GetMapping("/user/diet") // 사용자별 식단 목록 조회
    public List<DietDTO> getUserDiets(@AuthenticationPrincipal UserDetails userDetails) {
        return dietService.getDietsByUserId(userDetails);
    }

    @GetMapping("/diet/{date}") // 일자별 식단 목록 조회
    public ResponseEntity<List<DietDTO>> getDietByDate(@PathVariable String date) {
        List<DietDTO> dietList = dietService.getDietByDate(date);
        return ResponseEntity.ok(dietList);
    }

    @PostMapping("/diet") // 식단 작성
    public ResponseEntity<DietResponseDTO> createDiet(@RequestBody DietDTO dietDTO) {
        // 현재 사용자의 UserDetails를 가져옴
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        DietResponseDTO dietResponseDTO = dietService.createDiet(dietDTO, userDetails);
        return ResponseEntity.ok(dietResponseDTO);
    }

    @PutMapping("/diet/update/{dietId}")
    public ResponseEntity<DietResponseDTO> updateDiet(@PathVariable int dietId, @RequestBody DietDTO dietDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        DietResponseDTO updatedDiet = dietService.updateDiet(dietId, dietDTO, userDetails);
        DietResponseDTO dietResponseDTO = convertToResponseDTO(updatedDiet);
        return ResponseEntity.ok(dietResponseDTO);
    }

    @DeleteMapping("/diet/delete/{dietId}")
    public ResponseEntity<Void> deleteDiet(@PathVariable int dietId) {
        dietService.deleteDiet(dietId);
        return ResponseEntity.noContent().build();
    }

    private DietResponseDTO convertToResponseDTO(DietResponseDTO dietEntity) {
        DietResponseDTO responseDTO = new DietResponseDTO();
        responseDTO.setDietId(dietEntity.getDietId());
        responseDTO.setDate(dietEntity.getDate().toString());
        responseDTO.setType(dietEntity.getType());
        responseDTO.setWeek(dietEntity.getWeek());
        responseDTO.setMenus(dietEntity.getMenus().stream().map(menuEntity -> {
            MenuResponseDTO menuResponseDTO = new MenuResponseDTO();
            menuResponseDTO.setMenuId(menuEntity.getMenuId());
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
