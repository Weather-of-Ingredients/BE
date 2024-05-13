package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.diet.DietDTO;
import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.enums.DietType;
import com.nutritionangel.woi.repository.DietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DietService {

    private final DietRepository dietRepository;

    @Autowired
    public DietService(DietRepository dietRepository) { this.dietRepository = dietRepository; }

    // Diet 생성 후 저장
    public DietDTO createDiet(DietDTO dietDTO) {
        DietEntity dietEntity = mapToEntity(dietDTO);
        DietEntity savedEntity = dietRepository.save(dietEntity);
        return mapToDTO(savedEntity);
    }

    private DietEntity mapToEntity(DietDTO dietDTO) {
        return DietEntity.builder()
                .week(dietDTO.getWeek())
                .date(LocalDateTime.now())
                .type(DietType.valueOf(dietDTO.getType()))
                .build();
    }

    private DietDTO mapToDTO(DietEntity dietEntity) {
        return DietDTO.builder()
                .dietId(dietEntity.getDietId())
                .week(dietEntity.getWeek())
                .date(dietEntity.getDate().toString())
                .type(dietEntity.getType().name())
                .build();
    }
}
