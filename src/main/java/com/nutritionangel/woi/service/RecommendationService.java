package com.nutritionangel.woi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritionangel.woi.dto.crops.CropItem;
import com.nutritionangel.woi.dto.crops.CropItems;
import com.nutritionangel.woi.dto.recommendation.RecommendationDTO;
import com.nutritionangel.woi.entity.RecommendationEntity;
import com.nutritionangel.woi.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    //entity를 dto로 mapping
    private RecommendationDTO mapToDTO(int year, int month, RecommendationEntity recommendation) {
        recommendationDTO = new RecommendationDTO();
        recommendationDTO.setYear(year);
        recommendationDTO.setMonth(month);
        recommendationDTO.setGood_crop(recommendation.getGood_crop());
        recommendationDTO.setBad_crop(recommendation.getBad_crop());
        recommendationDTO.setAlt_crop(recommendation.getAlt_crop());

        return recommendationDTO;
    }

    public RecommendationDTO createRecommendationDTOService(String crop_type, RecommendationDTO recommendationDTO) {
        //year, month 로 만들어져있는지 검토 후 없으면 recommendationDTO -> cropItem
        //있으면 cropItem 에 붙여넣기
        if(getRecommendation(recommendationDTO.getYear(), recommendationDTO.getMonth())
                .isPresent()){
            //cropItem에 넣기
            return fillCropItems(crop_type, getRecommendation(recommendationDTO.getYear(), recommendationDTO.getMonth()).get(), recommendationDTO);
        }else{
            //없는 경우 Entity 만들고 cropItem에 넣기
            //recommendationEntity 만들기
            RecommendationEntity recommendEntity = mapToEntity(recommendationDTO);
            RecommendationEntity saveEntity = recommendationRepository.save(recommendEntity);
            //cropItem에 넣기
            return fillCropItems(crop_type, saveEntity, recommendationDTO);
        }
    }

    private RecommendationDTO fillCropItems(String crop_type, RecommendationEntity recommendationEntity, RecommendationDTO recommendationDTO) {
        switch (crop_type) {
            case "good_crop" -> {
                if (recommendationEntity.getGood_crop() == null) {
                    List<CropItem> cropItems = new ArrayList<>();
                    cropItems.add(recommendationDTO.getGood_crop().get(0));
                    recommendationEntity.setGood_crop(cropItems);
                } else {
                    recommendationEntity.getGood_crop().add(recommendationDTO.getGood_crop().get(0));
                    recommendationEntity.setGood_crop(recommendationEntity.getGood_crop());
                }
            }
            case "bad_crop" -> {
                if (recommendationEntity.getBad_crop() == null) {
                    List<CropItem> cropItems = new ArrayList<>();
                    cropItems.add(recommendationDTO.getBad_crop().get(0));
                    recommendationEntity.setBad_crop(cropItems);
                } else {
                    recommendationEntity.getBad_crop().add(recommendationDTO.getBad_crop().get(0));
                    recommendationEntity.setBad_crop(recommendationEntity.getBad_crop());
                }

            }
            case "alt_crop" -> {
                if (recommendationEntity.getAlt_crop() == null) {
                    List<CropItem> cropItems = new ArrayList<>();
                    cropItems.add(recommendationDTO.getAlt_crop().get(0));
                    recommendationEntity.setAlt_crop(cropItems);
                } else {
                    recommendationEntity.getAlt_crop().add(recommendationDTO.getAlt_crop().get(0));
                    recommendationEntity.setAlt_crop(recommendationEntity.getAlt_crop());
                }
            }
            default -> {
                return null;
            }
        }
        recommendationRepository.save(recommendationEntity);
        return mapToDTO(recommendationDTO.getYear(), recommendationDTO.getMonth(), recommendationEntity);
    }

    private RecommendationEntity mapToEntity(RecommendationDTO recommendationDTO) {
        return RecommendationEntity.builder()
                .year(recommendationDTO.getYear())
                .month(recommendationDTO.getMonth())
                .build();
    }
}
