package fr.bimiot.application.dtos;

import lombok.Data;

@Data
public class ProjectApiGetAllResponse {
    private String name;
    private String ifcFilename;
    private String datasetFilename;
}
