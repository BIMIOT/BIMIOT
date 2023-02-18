package fr.bimiot.application;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SensorColorsApi {
    private ArrayList<String> colors;
    private ArrayList<Float> values;
}
