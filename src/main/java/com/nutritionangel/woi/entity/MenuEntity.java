package com.nutritionangel.woi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Integer menuId;

    @Column(nullable = false)
    private String ingredients;

    @Column(nullable = false)
    private String carbohydrate;

    @Column(nullable = false)
    private String protein;

    @Column(nullable = false)
    private String fat;

    @Column(nullable = false)
    private String food_name;

    @Column(nullable = false)
    private String calories;

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private DietEntity diet;
}
