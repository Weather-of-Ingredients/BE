package com.nutritionangel.woi.repository;

import com.nutritionangel.woi.entity.DietEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<DietEntity, Integer> {
    List<DietEntity> findByDate(LocalDate parse);
}
