package com.nutritionangel.woi.dto.diet;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuDTO {
    private String ingredients;
    private String carbohydrate;
    private String protein;
    private String fat;
    private String food_name;
    private String calories;

    public String getFoodName() {
        return food_name;
    }
}
