package com.nutritionangel.woi.repository;

import com.nutritionangel.woi.dto.diet.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
