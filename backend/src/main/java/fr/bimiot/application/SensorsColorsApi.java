package fr.bimiot.application;

import lombok.Data;

import java.util.Map;

@Data
public class SensorsColorsApi {
    private Map<String, SensorColorsApi> sensorsColors;
}
