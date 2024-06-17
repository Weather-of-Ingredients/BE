package com.nutritionangel.woi.dto.diet;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class HomeDTO {
    // 홈 화면에 반환할 Dto
    private List<DietDTO> dietList;
}
