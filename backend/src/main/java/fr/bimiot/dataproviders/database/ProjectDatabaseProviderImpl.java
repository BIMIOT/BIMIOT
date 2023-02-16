package fr.bimiot.dataproviders.database;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ProjectDatabaseProviderImpl implements ProjectDatabaseProvider {

    private final ProjectJpaRepository projectJpaRepository;

    private final MongoOperations mongoOperations;


    public ProjectDatabaseProviderImpl(ProjectJpaRepository projectJpaRepository, MongoOperations mongoOperations) {
        this.projectJpaRepository = projectJpaRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public String create(Project project) throws IOException {
        return projectJpaRepository.insert(toProjectJpa(project)).getId();
    }

    @Override
    public void deleteByName(String projectName) {
//        var projectList = projectJpaRepository.findAll();
//        for (ProjectJpa project : projectList) {
//            System.out.println("project name is :"+project.getName());
//            if (project.getName().equals(projectName)){
//                System.out.println("deleted project name is :"+project.getName());
//                projectJpaRepository.deleteById(project.getId());
//            }
//        }

        //projectJpaRepository.deleteById("63edf6c7d751e81d1136debd");
        System.out.println("project name is :"+projectName);
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(projectName));
        mongoOperations.remove(query, ProjectJpa.class);

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
