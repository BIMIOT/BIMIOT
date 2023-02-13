package fr.bimiot.infrastructure;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ProjectJpa {
    @Id
    private Long id;
    private String name;

    public ProjectJpa(String name) {
        this.name = name;
    }
}
