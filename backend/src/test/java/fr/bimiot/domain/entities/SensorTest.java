package fr.bimiot.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SensorTest {
    @Test
    void instantiate_shouldHasCorrectFields() {
        var sensor = new Sensor("sensorDatasetId", "sensorIFCId", SensorType.LIGHT, "10.5");

        assertEquals("sensorDatasetId", sensor.getSensorDataSetId());
        assertEquals("sensorIFCId", sensor.getSensorIFCid());
        assertEquals(SensorType.LIGHT, sensor.getType());
        assertEquals("10.5", sensor.getValue());
    }

    @Test
    void equals_shouldBeTrue() {
        var sensor1 = new Sensor("sensorDatasetId", "sensorIFCId", SensorType.LIGHT, "10.5");
        var sensor2 = new Sensor("sensorDatasetId", "sensorIFCId", SensorType.LIGHT, "10.5");

        assertEquals(sensor1, sensor2);
    }
}