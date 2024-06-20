package com.nutritionangel.woi.dto.diet;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nutritionangel.woi.entity.UserEntity;
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
    private Integer userId; // 사용자 ID 추가
}
