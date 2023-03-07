package fr.bimiot.fixtures;

import fr.bimiot.domain.entities.Sensor;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.utils.Builder;

public class SensorFixture {
    public static Sensor aSimpleSensor() {
        return Builder.of(Sensor::new)
                .with(Sensor::setValue, "10")
                .with(Sensor::setType, SensorType.LIGHT)
                .with(Sensor::setSensorIFCid, "ifcId")
                .with(Sensor::setSensorDataSetId, "datasetId")
                .build();
    }
}
