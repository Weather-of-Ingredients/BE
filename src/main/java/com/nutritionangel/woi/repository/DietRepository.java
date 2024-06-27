package com.nutritionangel.woi.repository;

import com.nutritionangel.woi.entity.DietEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<DietEntity, Integer> {
    List<DietEntity> findByDate(LocalDate parse);

    List<DietEntity> findByUserUserId(Integer userId); // 사용자 ID로 식단 검색

    @Query(value = "SELECT * FROM diet WHERE YEAR(date) = :year AND MONTH(date) = :month AND USER_ID = :user_id", nativeQuery = true)
    List<DietEntity> findByYearAndMonthWithUser(@Param("user_id") Integer userId, @Param("year")int year, @Param("month") int month);

}
