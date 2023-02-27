package fr.bimiot.dataproviders.database;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Document(collection = "project")
public class ProjectJpa {
    @Id
    private String id;
    private String name;
    private Binary ifc;
    private Binary dataset;
    private Map<SensorTypeJpa, List<SensorColorJpa>> sensorColorJpaMap;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectJpa that = (ProjectJpa) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
