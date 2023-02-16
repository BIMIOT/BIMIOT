package fr.bimiot.dataproviders.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJpaRepository extends MongoRepository<ProjectJpa, String> {

}