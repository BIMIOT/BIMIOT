package fr.bimiot.application;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProjectApi {
    private String id;
    private String name;
    private Map<String, List<SensorColorApi>> sensorsColorsApi;
}
