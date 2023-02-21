package fr.bimiot.dataproviders.database;

import lombok.Data;

import java.util.Map;

@Data
public class SensorsColorsJpa {
    private Map<String, SensorColorsJpa> sensorsColorsJpa;
}
