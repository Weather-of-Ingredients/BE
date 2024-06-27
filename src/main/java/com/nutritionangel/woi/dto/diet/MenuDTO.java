package com.nutritionangel.woi.dto.diet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuDTO {
    @Getter
    private Integer menuId;
    private double carbohydrate;
    private double protein;
    private double fat;
    private String foodName;
    private double calories;
    private DietDTO diet;

}
