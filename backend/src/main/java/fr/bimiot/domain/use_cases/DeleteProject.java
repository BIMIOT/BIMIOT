package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import fr.bimiot.domain.use_cases.simulation.DeleteSimulation;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectProvider projectProvider;
    private final DeleteSimulation deleteSimulation;

    public DeleteProject(ProjectProvider projectProvider, DeleteSimulation deleteSimulation) {
        this.projectProvider = projectProvider;
        this.deleteSimulation = deleteSimulation;
    }

    public void execute(String projectName) throws DomainException {
        deleteSimulation.execute(projectName);
        projectProvider.delete(projectName);
    }
}
