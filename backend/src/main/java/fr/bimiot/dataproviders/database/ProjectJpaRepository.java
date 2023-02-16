package fr.bimiot.dataproviders.database;

import fr.bimiot.domain.entities.Project;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJpaRepository extends MongoRepository<ProjectJpa, String>{
    @Query("{'name': ?0 }")
    ProjectJpa findByName(String projectName);

}
