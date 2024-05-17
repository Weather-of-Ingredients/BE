package com.nutritionangel.woi.dto.diet;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DietDTO {
    private Integer dietId;
    private String date;
    private String type;
    private String week;
}
