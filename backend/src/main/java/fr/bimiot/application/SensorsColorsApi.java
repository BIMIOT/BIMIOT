package fr.bimiot.application;

import lombok.Data;

import java.util.HashMap;

@Data
public class SensorsColorsApi {
    private HashMap<SensorTypeApi, SensorColorsApi> sensorsColors;
}
