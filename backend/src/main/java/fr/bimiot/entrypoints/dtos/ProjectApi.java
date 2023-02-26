package fr.bimiot.entrypoints.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ProjectApi {
    private String id;
    private String name;
    private byte[] ifcFileContent;
    private Map<SensorTypeApi, List<SensorColorApi>> sensorsColorsApi;
}
