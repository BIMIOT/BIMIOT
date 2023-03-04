package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import fr.bimiot.domain.use_cases.simulation.CreateSimulation;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class CreateProject {

    private final ProjectProvider projectProvider;
    private final CreateSimulation createSimulation;

    public CreateProject(ProjectProvider projectProvider, CreateSimulation createSimulation) {
        this.projectProvider = projectProvider;
        this.createSimulation = createSimulation;
    }

    public String execute(Project project, MultipartFile dataset) throws DomainException, IOException {
        if (projectProvider.isExistedProject(project.getName())) {
            throw new DomainException("Project '" + project.getName() + "' already exists !");
        }
        try {
            createSimulation.execute(project.getName(), dataset);
        } catch (RestClientException e) {
            throw new DomainException("Problem with simulator : address/port or json dataset");
        }
        return projectProvider.create(project);
    }


}
