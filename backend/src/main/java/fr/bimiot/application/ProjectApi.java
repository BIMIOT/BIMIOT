package fr.bimiot.application;

import lombok.Data;

@Data
public class ProjectApi {
    private String id;
    private String name;
    private SensorsColorsApi sensorsColorsApi;
}
