package fr.bimiot.dataproviders.database;

import lombok.Data;

import java.util.List;

@Data
public class SensorColorsJpa {
    private List<String> colors;
    private List<Float> values;
}
