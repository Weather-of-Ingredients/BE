package com.nutritionangel.woi.entity;

import com.nutritionangel.woi.dto.crops.CropItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Entity
@Table(name="Recommendation")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recommendation_id")
    private int id;
    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Embedded
    @ElementCollection
    @CollectionTable(name = "good_crop", joinColumns = @JoinColumn(name = "recommendation_id"))
    private List<CropItem> good_crop;


    @Embedded
    @ElementCollection
    @CollectionTable(name = "bad_crop", joinColumns = @JoinColumn(name = "recommendation_id"))
    private List<CropItem> bad_crop;


    @Embedded
    @ElementCollection
    @CollectionTable(name = "alt_crop", joinColumns = @JoinColumn(name = "recommendation_id"))
    private List<CropItem> alt_crop;
}
