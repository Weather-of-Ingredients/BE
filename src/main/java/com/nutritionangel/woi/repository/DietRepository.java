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

    @Query(value = "SELECT * FROM diet WHERE YEAR(date) = :year AND MONTH(date) = :month", nativeQuery = true)
    List<DietEntity> findByYearAndMonth(@Param("year")int year, @Param("month") int month);
}
