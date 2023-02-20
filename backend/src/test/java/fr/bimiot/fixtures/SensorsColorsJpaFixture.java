package fr.bimiot.fixtures;

import fr.bimiot.dataproviders.database.SensorColorsJpa;
import fr.bimiot.dataproviders.database.SensorsColorsJpa;
import fr.bimiot.domain.entities.SensorType;

import java.util.HashMap;
import java.util.List;

public class SensorsColorsJpaFixture {
    public static SensorsColorsJpa aCompleteSensorsColorsJpa() {
        SensorsColorsJpa sensorsColorsJpa = new SensorsColorsJpa();
        HashMap<String, SensorColorsJpa> map = new HashMap<>();
        map.put(SensorType.TEMPERATURE.name(), aSimpleSensorColors());
        map.put(SensorType.CO2.name(), aSimpleSensorColors());
        map.put(SensorType.HUMIDITY.name(), aSimpleSensorColors());
        map.put(SensorType.LIGHT.name(), aSimpleSensorColors());
        sensorsColorsJpa.setSensorsColorsJpa(map);
        return sensorsColorsJpa;
    }

    private static SensorColorsJpa aSimpleSensorColors() {
        SensorColorsJpa sensorColorsJpa = new SensorColorsJpa();
        sensorColorsJpa.setColors(List.of("#219ebc", "#ffb703"));
        sensorColorsJpa.setValues(List.of(10f));
        return sensorColorsJpa;
    }
}
