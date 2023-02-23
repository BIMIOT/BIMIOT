package fr.bimiot.domain.use_cases;

import fr.bimiot.application.exception.type.BaseException;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

@Component
public class LoadIfcFile {
    private final ProjectDatabaseProvider projectDatabaseProvider;

    public LoadIfcFile(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public byte[] execute(String projectName) throws BaseException {
        if (isInvalidProjectName(projectName)) {
            throw new DomainException("Invalid project name !");
        }
        return projectDatabaseProvider.loadIFCFile(projectName);
    }

    private boolean isInvalidProjectName(String projectName) {
        return projectName == null || projectName.isBlank() || projectName.isEmpty();
    }
}
