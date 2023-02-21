package fr.bimiot.dataproviders.database;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "project")
public class ProjectJpa {
    @Id
    private String id;
    private String name;
    private Binary ifc;
    private Binary dataset;
    private Map<String, List<SensorColorJpa>> sensorColorJpaMap;
}
