package fr.bimiot.fixtures;

import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;

import java.util.*;

public class SensorColorMapFixture {

    public static Map<SensorType, List<SensorColor>> sensorTypeListMapDomain() {
        var map = new HashMap<SensorType, List<SensorColor>>();
        Arrays.stream(SensorType.values())
                .forEach(sensorType -> map.put(sensorType, aCompletePaletteSensorColor()));
        return map;
    }

    private static List<SensorColor> aCompletePaletteSensorColor() {
        var list = new ArrayList<SensorColor>();
        list.add(new SensorColor("#8ecae6", Float.NEGATIVE_INFINITY, 10f));
        list.add(new SensorColor("#219ebc", 10f, 20f));
        list.add(new SensorColor("#023047", 20f, 30f));
        list.add(new SensorColor("#ffb703", 30f, Float.POSITIVE_INFINITY));
        return list;
    }
}
