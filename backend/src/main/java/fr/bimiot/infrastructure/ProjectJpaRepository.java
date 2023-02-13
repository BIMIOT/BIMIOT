package fr.bimiot.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectJpaRepository extends MongoRepository<ProjectJpa, Long> {
}
