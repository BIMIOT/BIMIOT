package fr.bimiot.domain.use_cases;

import fr.bimiot.dataproviders.database.ProjectDatabaseProviderImpl;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectFileProvider projectFileProvider;

    private final ProjectDatabaseProviderImpl projectDatabaseProvider;

    public DeleteProject(ProjectFileProvider projectFileProvider, ProjectDatabaseProviderImpl projectDatabaseProvider) {
        this.projectFileProvider = projectFileProvider;
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public void execute(String projectName) {
        //projectFileProvider.delete(projectName);
        projectDatabaseProvider.deleteByName(projectName);
    }

}
