package com.nutritionangel.woi.controller;

//import com.nutritionangel.woi.entity.CropItem;
//import com.nutritionangel.woi.entity.CropItems;
//import com.nutritionangel.woi.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/crops")
public class RecommendationController {

//    private final RecommendationService recommendationService;

    @Value("${openApi.cropServiceKey}")
    private String serviceKey2;
    @Value("${openApi.cropCallBackUrl}")
    private String apiUrl;

    HttpURLConnection urlConnection = null;
    InputStream stream = null;
    String result = null;
//    private CropItems cropItems;
//
//
//    @Autowired
//    public RecommendationController(RecommendationService recommendationService) {
//        this.recommendationService = recommendationService;
//    }
   /* @GetMapping("")
    public List<CropItem> callApi() throws IOException {
        String urlStr = apiUrl +
                serviceKey +
                "/json/Grid_20171128000000000572_1/1/10";

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
    }*/


    @GetMapping("/{product_name}")
    public String callApi2(@PathVariable("product_name") String product_name) throws IOException {
        String crop_name = URLEncoder.encode(product_name, StandardCharsets.UTF_8);

        System.out.println(crop_name);

        String urlStr = apiUrl +
                serviceKey2 +
                "/json/Grid_20171128000000000572_1/1/5?PRDLST_NM=" + crop_name;

        try{
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            stream = getNetworkConnection(urlConnection);
            result = readStreamToString(stream);

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
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