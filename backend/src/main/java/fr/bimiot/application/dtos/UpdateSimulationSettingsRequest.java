package fr.bimiot.application.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonPropertyOrder({"host", "port"})
public class UpdateSimulationSettingsRequest {
    @ApiModelProperty(position = 0)
    private String host;
    @ApiModelProperty(position = 1)
    private Integer port;
}
