package fr.bimiot.application.dtos;

import fr.bimiot.application.SensorTypeApi;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SensorColorApiMap {
    private Map<SensorTypeApi, List<SensorColorApi>> sensorColorApis;
}
