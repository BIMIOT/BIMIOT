package fr.bimiot.dataproviders.database;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "project")
public class ProjectJpa {
    @Id
    private String id;
    private String name;
    private Binary ifc;
    private Binary dataset;
    public ProjectJpa(String name, Binary ifc, Binary dataset) {
        this.name = name;
        this.ifc = ifc;
        this.dataset = dataset;
    }
}
