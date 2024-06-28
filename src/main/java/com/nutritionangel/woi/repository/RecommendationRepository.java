package com.nutritionangel.woi.repository;

import com.nutritionangel.woi.entity.RecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendationEntity, Integer> {
    List<RecommendationEntity> findByYearAndMonth(int year, int month);
}
