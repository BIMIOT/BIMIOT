package fr.bimiot.application.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProjectApi {
    private String id;
    private String name;
    private Map<SensorTypeApi, List<SensorColorApi>> sensorsColorsApi;
}
