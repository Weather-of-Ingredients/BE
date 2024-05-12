package com.nutritionangel.woi.repository;

import com.nutritionangel.woi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
