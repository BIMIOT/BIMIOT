package fr.bimiot.application;

import lombok.Data;

import java.util.List;

@Data
public class SensorColorsApi {
    private List<String> colors;
    private List<Float> values;
}
