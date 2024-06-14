package com.nutritionangel.woi.dto.crops;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class CropItem {

    @JsonProperty("PRDLST_NM")
    private String ingredient_name;
    @JsonProperty("IMG_URL")
    private String ingredient_image;
}
