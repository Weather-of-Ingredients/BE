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

//    @Column(nullable = false)
    private double carbohydrate;

//    @Column(nullable = false)
    private double protein;

//    @Column(nullable = false)
    private double fat;

//    @Column(nullable = false)
    private String foodName;

//    @Column(nullable = false)
    private double calories;

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private DietEntity diet;
}
