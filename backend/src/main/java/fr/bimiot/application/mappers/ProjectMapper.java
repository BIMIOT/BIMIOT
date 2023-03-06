package fr.bimiot.application.mappers;

import fr.bimiot.application.dtos.ProjectApiGetAllResponse;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.utils.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ProjectMapper {
    private ProjectMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ProjectApiGetAllResponse toProjectApiGetAllResponse(Project project) {
        return Builder.of(ProjectApiGetAllResponse::new)
                .with(ProjectApiGetAllResponse::setName, project.getName())
                .with(ProjectApiGetAllResponse::setIfcFilename, project.getIfcFilename())
                .with(ProjectApiGetAllResponse::setDatasetFilename, project.getDatasetFilename())
                .build();
    }

    public static Project toProject(String projectName, MultipartFile ifc, MultipartFile dataset) throws IOException {
        return Builder.of(Project::new)
                .with(Project::setName, projectName)
                .with(Project::setIfcFile, ifc.getBytes())
                .with(Project::setIfcFilename, ifc.getOriginalFilename())
                .with(Project::setDatasetFilename, dataset.getOriginalFilename())
                .build();
    }
}
