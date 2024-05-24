package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.crops.CropItem;
import com.nutritionangel.woi.dto.crops.CropItems;
import com.nutritionangel.woi.dto.recommendation.RecommendationDTO;
import com.nutritionangel.woi.dto.response.CropResponseDTO;
import com.nutritionangel.woi.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @GetMapping("/{year}/{month}")
    public CropResponseDTO getRecommendationDTO(@PathVariable("year") int year, @PathVariable("month") int month){
        RecommendationDTO recommendationDTO = recommendationService.getRecommendationDTOService(year, month);
        if(recommendationDTO == null){
            return new CropResponseDTO(false, null);
        }
        return new CropResponseDTO(true, recommendationDTO);
    }

    @GetMapping("")
    public List<CropItem> callApi() throws IOException {

        String urlStr = callBackUrl +
                serviceKey + "/" +
                dataType + "/" +
                ApiUrl + "/" +
                cropStart + "/" +
                cropEnd;

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
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String readLine;
        while((readLine = br.readLine()) != null) {
            result.append(readLine + "\n\r");
        }

        br.close();

        return result.toString();
    }

}
