package com.nutritionangel.woi.entity;

import com.nutritionangel.woi.enums.DietType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "diet")
public class DietEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diet_id")
    private Integer dietId;

    @CreatedDate
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private DietType type;

    @Column(nullable = false)
    private String week;

    @OneToMany(mappedBy = "diet")
    private List<MenuEntity> menus;
}
