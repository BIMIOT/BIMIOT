package fr.bimiot.application.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SensorColorApiMap {
    private Map<String, List<SensorColorApi>> sensorColorApis;
}