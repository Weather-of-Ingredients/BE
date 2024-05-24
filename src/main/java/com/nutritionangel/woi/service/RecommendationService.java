package com.nutritionangel.woi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritionangel.woi.dto.crops.CropItems;
import com.nutritionangel.woi.dto.recommendation.RecommendationDTO;
import com.nutritionangel.woi.entity.RecommendationEntity;
import com.nutritionangel.woi.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private RecommendationDTO recommendationDTO;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public CropItems parsingJson(String json){
        CropItems items = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            items = mapper.readValue(json, CropItems.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }

    public RecommendationDTO getRecommendationDTOService(int year, int month) {
        return getRecommendation(year, month)
                .map(recommendation -> mapToDTO(year, month, recommendation))
                .orElse(null);
    }

    private Optional<RecommendationEntity> getRecommendation(int year, int month) {
        return recommendationRepository.findByYearAndMonth(year, month);
    }

    private RecommendationDTO mapToDTO(int year, int month, RecommendationEntity recommendation) {
        recommendationDTO = new RecommendationDTO();
        recommendationDTO.setYear(year);
        recommendationDTO.setMonth(month);
        recommendationDTO.setGood_crop(recommendation.getGood_crop());
        recommendationDTO.setBad_crop(recommendation.getBad_crop());
        recommendationDTO.setAlt_crop(recommendation.getAlt_crop());

        return recommendationDTO;
    }

}
