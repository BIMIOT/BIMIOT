package fr.bimiot.fixtures;

import fr.bimiot.dataproviders.database.ProjectJpa;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

public class ProjectJpaFixture {
    public static ProjectJpa aProjectJpaWithoutSensorsAndWithoutId() {
        ProjectJpa projectJpa = new ProjectJpa();
        projectJpa.setName("Project X");
        projectJpa.setIfc(new Binary(BsonBinarySubType.BINARY, "Hello World".getBytes()));
        projectJpa.setIfc(new Binary(BsonBinarySubType.BINARY, "Hello World".getBytes()));
        return projectJpa;
    }

    public static ProjectJpa aProjectJpaWithoutSensors() {
        ProjectJpa projectJpa = aProjectJpaWithoutSensorsAndWithoutId();
        projectJpa.setId("ProjectID");
        return projectJpa;
    }

    public static ProjectJpa aCompleteProjectJpa() {
        ProjectJpa projectJpa = aProjectJpaWithoutSensors();
        projectJpa.setSensorColorJpaMap(SensorColorJpaMapFixture.sensorTypeListMapDataProviders());
        return projectJpa;
    }

}
