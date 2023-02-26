package fr.bimiot.entrypoints.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UpdateProjectRequest {
    private String name;
    private byte[] ifcFileContent;
    private Map<SensorTypeApi, List<SensorColorApi>> sensorsColorsMap;
}
