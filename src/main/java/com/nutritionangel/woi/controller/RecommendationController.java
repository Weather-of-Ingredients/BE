package com.nutritionangel.woi.controller;


import com.nutritionangel.woi.dto.crops.CropItem;
import com.nutritionangel.woi.dto.crops.CropItems;
import com.nutritionangel.woi.dto.recommendation.RecommendationDTO;
import com.nutritionangel.woi.dto.response.BadCropMenuDTO;
import com.nutritionangel.woi.dto.response.CropResponseDTO;
import com.nutritionangel.woi.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/crops")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Value("${openApi.cropServiceKey}")
    private String serviceKey;
    @Value("${openApi.cropCallBackUrl}")
    private String callBackUrl;
    @Value("${openApi.dataType}")
    private String dataType;
    @Value("${openApi.cropApiUrl}")
    private String ApiUrl;
    @Value("${openApi.cropStart}")
    private String cropStart;
    @Value("${openApi.cropEnd}")
    private String cropEnd;

    HttpURLConnection urlConnection = null;
    InputStream stream = null;
    String result = null;

    private CropItems cropItems;


    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /*year 과 month 에 해당하는 good, bad, alt crop 보여주는 url
    * (홈화면 - 배너)
    * */
    @GetMapping("/{year}/{month}")
    public CropResponseDTO getRecommendationDTO(@PathVariable("year") int year, @PathVariable("month") int month){

        RecommendationDTO recommendationDTO = recommendationService.getRecommendationDTOService(year, month);
        if(recommendationDTO == null){
            return new CropResponseDTO(false, null);
        }
        return new CropResponseDTO(true, recommendationDTO);
    }

    /*
    * 어려운 작물 가져오기
    * (기후예측 - 어려운 작물 Get)
    * */
    @GetMapping("/{year}/{month}/{crop_type}")
    public CropResponseDTO getCrops(@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("crop_type") String crop_type){
        List<CropItem> cropItems = recommendationService.getCropService(year, month, crop_type);
        if(cropItems == null){
            return new CropResponseDTO(false, null);
        }
        return new CropResponseDTO(true, cropItems);
    }

    //OpenApi 연결
    @GetMapping("/{name}")
    public List<CropItem> callApi(@PathVariable("name") String name) throws IOException {

        String urlStr = callBackUrl +
                serviceKey + "/" +
                dataType + "/" +
                ApiUrl + "/" +
                cropStart + "/" +
                cropEnd + "?PRDLST_NM=" + URLEncoder.encode(name, StandardCharsets.UTF_8.toString());


        try{
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            stream = getNetworkConnection(urlConnection);
            result = readStreamToString(stream);
            cropItems = recommendationService.parsingJson(result);


        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return cropItems.getCropItems();
    }

    /*input year, month, crop type 들어오면
    * db에 저장하는 url
    * (기후예측 - post)
    * */
    @PostMapping("/add/{crop_type}")
    private CropResponseDTO addCrops(@PathVariable("crop_type") String crop_type, @RequestBody RecommendationDTO recommendationDTO){

        RecommendationDTO createRecommendationDTO = recommendationService.createRecommendationDTOService(crop_type, recommendationDTO);
        if(recommendationDTO == null){
            return new CropResponseDTO(false, null);
        }
        return new CropResponseDTO(true, createRecommendationDTO);

    }

    @GetMapping("/{year}/{month}/menus")
    private  List<BadCropMenuDTO> getBadCropsMenus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("year") int year, @PathVariable("month") int month){
        List<CropItem> badCropItems = recommendationService.getCropService(year, month, "bad_crop");
        List<CropItem> altCropItems = recommendationService.getCropService(year, month, "alt_crop");

        if(badCropItems == null && altCropItems == null){
            return Collections.emptyList();
        }
        List<BadCropMenuDTO> badCropMenuDTOList = recommendationService.getBadCropsMenus(userDetails, year, month, badCropItems, altCropItems);

        if(badCropMenuDTOList == null){
            return Collections.emptyList();
        }else{
            return badCropMenuDTOList;
        }
    }

    private InputStream getNetworkConnection(HttpURLConnection urlConnection) throws IOException{
        urlConnection.setConnectTimeout(3000); //연결타임아웃 시간
        urlConnection.setReadTimeout(3000);//읽기 타임아웃 시간
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);

        if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code : " + urlConnection.getResponseCode());
        }

        return urlConnection.getInputStream();
    }

    private String readStreamToString(InputStream stream) throws IOException{
        StringBuilder result = new StringBuilder();

        //문자 데이터를 읽기 위해 wrapping

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String readLine;
        while((readLine = br.readLine()) != null) {
            result.append(readLine + "\n\r");
        }

        br.close();

        return result.toString();
    }


}


