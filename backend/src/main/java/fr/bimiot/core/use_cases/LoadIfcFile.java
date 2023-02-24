package fr.bimiot.core.use_cases;

import fr.bimiot.core.exception.DomainException;
import fr.bimiot.core.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

@Component
public class LoadIfcFile {
    private final ProjectDatabaseProvider projectDatabaseProvider;

    public LoadIfcFile(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    /**
     * Load ifc file of project with name projectName
     * @param projectName of corresponding project
     * @return bytes array content of ifc project file
     * @throws DomainException throw this exception if projectName is invalid
     */
    //  TODO : move invalid project name check to ProjectController with spring boot validator
    public byte[] execute(String projectName) throws DomainException {
        if (isInvalidProjectName(projectName)) {
            throw new DomainException("Invalid project name !");
        }
        return projectDatabaseProvider.loadIFCFile(projectName);
    }

    private boolean isInvalidProjectName(String projectName) {
        return projectName == null || projectName.isBlank() || projectName.isEmpty();
    }
}
