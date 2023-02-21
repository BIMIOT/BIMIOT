package fr.bimiot.fixtures;

import fr.bimiot.dataproviders.database.SensorColorJpa;
import fr.bimiot.domain.entities.SensorType;

import java.util.*;

public class SensorColorJpaMapFixture {
    public static Map<String, List<SensorColorJpa>> sensorTypeListMapDataProviders() {
        var map = new HashMap<String, List<SensorColorJpa>>();
        Arrays.stream(SensorType.values())
                .forEach(sensorType -> map.put(sensorType.name(), aCompletePaletteSensorColor()));
        return map;
    }

    private static List<SensorColorJpa> aCompletePaletteSensorColor() {
        var list = new ArrayList<SensorColorJpa>();
        list.add(new SensorColorJpa("#8ecae6", Float.NEGATIVE_INFINITY, 10f));
        list.add(new SensorColorJpa("#219ebc", 10f, 20f));
        list.add(new SensorColorJpa("#023047", 20f, 30f));
        list.add(new SensorColorJpa("#ffb703", 30f, Float.POSITIVE_INFINITY));
        return list;
    }
}
