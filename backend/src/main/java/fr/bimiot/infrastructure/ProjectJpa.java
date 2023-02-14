package fr.bimiot.infrastructure;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "project")
public class ProjectJpa {
    @Id
    private String id;
    private String name;

    public ProjectJpa(String name) {
        this.name = name;
    }
}
