package fr.bimiot.core.use_cases;

import fr.bimiot.core.entities.Project;
import fr.bimiot.core.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CreateProject {

    private final ProjectDatabaseProvider projectDatabaseProvider;

    public CreateProject(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    /**
     * Create project in database
     * @param project has to create
     * @return project's id
     * @throws IOException throw if there is problem with getting bytes content file(s)
     */
    public String execute(Project project) throws IOException {
        return projectDatabaseProvider.create(project);
    }


}
