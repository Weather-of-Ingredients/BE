package com.nutritionangel.woi.dto.diet;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DietDTO {
    private Integer dietId;
    private String date;
    private String type;
    private String week;
    @Builder.Default
    private List<MenuDTO> menus = new ArrayList<>();
}
