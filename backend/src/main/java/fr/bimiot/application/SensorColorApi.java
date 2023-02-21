package fr.bimiot.application;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorColorApi {
    private String colorCode;
    private float rangeStart;
    private float rangeEnd;
}
