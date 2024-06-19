package com.nutritionangel.woi.dto.diet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("DESC_KOR")
    private String foodName;

    @JsonProperty("NUTR_CONT2")
    private double carbohydrate;

    @JsonProperty("NUTR_CONT3")
    private double protein;

    @JsonProperty("NUTR_CONT4")
    private double fat;

    @JsonProperty("NUTR_CONT1")
    private double calories;
}
