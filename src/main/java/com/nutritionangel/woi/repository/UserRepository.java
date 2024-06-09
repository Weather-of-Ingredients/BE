package com.nutritionangel.woi.repository;

import com.nutritionangel.woi.entity.UserEntity;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);

}
