package com.nutritionangel.woi.repository;

import com.nutritionangel.woi.entity.DietEntity;
import com.nutritionangel.woi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<DietEntity, Integer> {
    List<DietEntity> findByDate(LocalDate parse);

    @Query(value = "SELECT * FROM diet WHERE YEAR(date) = :year AND MONTH(date) = :month", nativeQuery = true)
    List<DietEntity> findByYearAndMonth(@Param("year")int year, @Param("month") int month);

    List<DietEntity> findByUserUserId(Integer userId); // 사용자 ID로 식단 검색

    DietEntity findByDietId(Integer dietId);

    List<DietEntity> findByUserAndDate(UserEntity userEntity, LocalDate date);
}
