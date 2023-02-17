package fr.bimiot.dataproviders.database;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ProjectDatabaseProviderImpl implements ProjectDatabaseProvider {

    private final ProjectJpaRepository projectJpaRepository;



    public ProjectDatabaseProviderImpl(ProjectJpaRepository projectJpaRepository) {
        this.projectJpaRepository = projectJpaRepository;
    }

    @Override
    public String create(Project project) throws IOException {
        return projectJpaRepository.insert(toProjectJpa(project)).getId();
    }

    @Override
    public void delete(String projectName) {
        projectJpaRepository.deleteByName(projectName);
    }

    private ProjectJpa toProjectJpa(Project project) throws IOException {
        ProjectJpa projectJpa = new ProjectJpa();
        projectJpa.setIfc(toBinary(project.getIfc()));
        projectJpa.setDataset(toBinary(project.getDataset()));
        projectJpa.setName(project.getName());
        return projectJpa;
    }

    private Binary toBinary(MultipartFile file) throws IOException {
        return new Binary(BsonBinarySubType.BINARY, file.getBytes());
    }
}
