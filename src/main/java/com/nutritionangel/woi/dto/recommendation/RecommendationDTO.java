package com.nutritionangel.woi.dto.recommendation;

import com.nutritionangel.woi.dto.crops.CropItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {

    @NotNull
    private int year;
    @NotNull
    private int month;

    private List<CropItem> good_crop;
    private List<CropItem> bad_crop;
    private List<CropItem> alt_crop;

}
