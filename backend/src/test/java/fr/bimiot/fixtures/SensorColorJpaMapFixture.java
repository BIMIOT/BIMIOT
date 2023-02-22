package fr.bimiot.fixtures;

import fr.bimiot.dataproviders.database.SensorColorJpa;
import fr.bimiot.dataproviders.database.SensorTypeJpa;
import fr.bimiot.domain.entities.SensorType;

import java.util.*;

public class SensorColorJpaMapFixture {
    public static Map<SensorTypeJpa, List<SensorColorJpa>> sensorTypeListMapDataProviders() {
        var map = new HashMap<SensorTypeJpa, List<SensorColorJpa>>();
        Arrays.stream(SensorType.values())
                .forEach(sensorType -> map.put(SensorTypeJpa.valueOf(sensorType.name()), aCompletePaletteSensorColor()));
        return map;
    }

    private static List<SensorColorJpa> aCompletePaletteSensorColor() {
        var list = new ArrayList<SensorColorJpa>();
        list.add(new SensorColorJpa("#8ecae6", 10f));
        list.add(new SensorColorJpa("#219ebc", 20f));
        list.add(new SensorColorJpa("#023047", 30f));
        list.add(new SensorColorJpa("#ffb703", Float.POSITIVE_INFINITY));
        return list;
    }
}