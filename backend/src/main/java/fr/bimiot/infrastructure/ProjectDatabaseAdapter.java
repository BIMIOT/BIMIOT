package fr.bimiot.infrastructure;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.use_cases.ProjectPort;
import org.springframework.stereotype.Component;

@Component
public class ProjectDatabaseAdapter implements ProjectPort {

    private final ProjectJpaRepository projectJpaRepository;

    public ProjectDatabaseAdapter(ProjectJpaRepository projectJpaRepository) {
        this.projectJpaRepository = projectJpaRepository;
    }

    @Override
    public void insert(Project project) {
        projectJpaRepository.insert(toProjectJpa(project));
    }

    private ProjectJpa toProjectJpa(Project project){
        return new ProjectJpa(project.name());
    }
}
