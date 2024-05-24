package com.nutritionangel.woi.dto.crops;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import java.util.Arrays;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CropItems {
    @JsonProperty("row")
    private List<CropItem> cropItems;

    @JsonCreator
    public CropItems(@JsonProperty("Grid_20171128000000000572_1") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = node.findValue("row");
        this.cropItems = Arrays.stream(objectMapper.treeToValue(itemNode, CropItem[].class)).toList();
    }
}



