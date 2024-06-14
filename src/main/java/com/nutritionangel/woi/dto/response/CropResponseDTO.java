package com.nutritionangel.woi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CropResponseDTO<T> {
    private Boolean success;
    private T data;

}
