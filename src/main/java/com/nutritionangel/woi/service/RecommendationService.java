package com.nutritionangel.woi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritionangel.woi.dto.crops.CropItem;
import com.nutritionangel.woi.dto.crops.CropItems;
import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.dto.diet.DietResponseDTO;
import com.nutritionangel.woi.dto.diet.MenuResponseDTO;
import com.nutritionangel.woi.dto.recommendation.RecommendationDTO;
import com.nutritionangel.woi.dto.response.BadCropMenuDTO;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.entity.RecommendationEntity;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.repository.DietRepository;
import com.nutritionangel.woi.repository.RecommendationRepository;
import com.nutritionangel.woi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private RecommendationDTO recommendationDTO;

    private final DietRepository dietRepository;
    private final UserRepository userRepository;

    private DietService dietService;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository, DietRepository dietRepository, UserRepository userRepository, DietService dietService) {
        this.recommendationRepository = recommendationRepository;
        this.dietRepository = dietRepository;
        this.userRepository = userRepository;
        this.dietService = dietService;
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
        List<RecommendationEntity> recommendations = recommendationRepository.findByYearAndMonth(year, month);
        if (recommendations.size() == 1) {
            return Optional.of(recommendations.get(0));
        } else if (recommendations.isEmpty()) {
            return Optional.empty();
        } else {
            throw new IllegalStateException("Expected one result, but got: " + recommendations.size());
        }
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

    public RecommendationDTO createRecommendationDTOService(String crop_type, RecommendationDTO recommendationDTO) {
        if(getRecommendation(recommendationDTO.getYear(), recommendationDTO.getMonth())
                .isPresent()){
            return fillCropItems(crop_type, getRecommendation(recommendationDTO.getYear(), recommendationDTO.getMonth()).get(), recommendationDTO);
        }else{
            RecommendationEntity recommendEntity = mapToEntity(recommendationDTO);
            RecommendationEntity saveEntity = recommendationRepository.save(recommendEntity);
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

    public List<CropItem> getCropService(int year, int month, String crop_type) {

        Optional<RecommendationEntity> recommendationEntity = getRecommendation(year, month);


        if(recommendationEntity.isPresent()){
            RecommendationEntity recommendation = recommendationEntity.get();
            if(crop_type.equals("good_crop")){
                return recommendation.getGood_crop();
            } else if (crop_type.equals("bad_crop")) {
                return recommendation.getBad_crop();
            } else if (crop_type.equals("alt_crop")) {
                return recommendation.getAlt_crop();
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public List<BadCropMenuDTO> getBadCropsMenus(UserDetails userDetails, int year, int month, List<CropItem> badCropItems, List<CropItem> altCropItems) {
        List<BadCropMenuDTO> badCropMenuDTOList = new ArrayList<>();
        List<DietResponseDTO> dietResponseDTOList = getDietListByUser(userDetails, year, month);

        if(dietResponseDTOList == null){
            return null;
        }else{
            for(DietResponseDTO dietResponseDTO : dietResponseDTOList){
                String date = dietResponseDTO.getDate();
                int day = Integer.parseInt(date.split("-")[2]);

                List<MenuResponseDTO> menuResponseDTOList = dietResponseDTO.getMenus();

                for(MenuResponseDTO menuResponseDTO : menuResponseDTOList){
                    String foodName = menuResponseDTO.getFoodName();

                    for(int i = 0; i<badCropItems.size(); i++){

                        if(foodName.matches(".*" + badCropItems.get(i).getIngredient_name() + ".*")){

                            BadCropMenuDTO badCropMenuDTO = new BadCropMenuDTO();
                            badCropMenuDTO.setYear(year);
                            badCropMenuDTO.setMonth(month);
                            badCropMenuDTO.setDay(day);

                            badCropMenuDTO.setFood_name(foodName);
                            badCropMenuDTO.setBad_crop_name(badCropItems.get(i).getIngredient_name());
                            badCropMenuDTO.setAlt_crop_name(altCropItems.get(i).getIngredient_name());
                            badCropMenuDTOList.add(badCropMenuDTO);
                        }

                    }
                }


            }
            return badCropMenuDTOList;
        }
    }

    private List<DietResponseDTO> getDietListByUser(UserDetails userDetails, int year, int month){
        UserEntity userEntity = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<DietEntity> dietEntityList = dietRepository.findByYearAndMonthWithUser(userEntity.getUserId(), year, month);


        List<DietResponseDTO> dietResponseDTOList = new ArrayList<>();

        for(DietEntity dietEntity : dietEntityList){
            dietResponseDTOList.add(dietService.callToResponseDTO(dietEntity));
        }

        return dietResponseDTOList;
    }

}
