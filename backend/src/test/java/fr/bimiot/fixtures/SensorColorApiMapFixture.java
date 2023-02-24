package fr.bimiot.fixtures;

import fr.bimiot.application.SensorTypeApi;
import fr.bimiot.application.dtos.SensorColorApi;

import java.util.*;

public class SensorColorApiMapFixture {
    public static Map<String, List<SensorColorApi>> sensorTypeListMapEntrypoint() {
        var map = new HashMap<String, List<SensorColorApi>>();
        Arrays.stream(SensorTypeApi.values())
                .forEach(sensorTypeApi -> map.put(sensorTypeApi.name(), aCompletePaletteSensorColorApi()));
        return map;
    }

    private static List<SensorColorApi> aCompletePaletteSensorColorApi() {
        var list = new ArrayList<SensorColorApi>();
        list.add(new SensorColorApi("#8ecae6", 10f));
        list.add(new SensorColorApi("#219ebc", 20f));
        list.add(new SensorColorApi("#023047", 30f));
        list.add(new SensorColorApi("#ffb703", Float.POSITIVE_INFINITY));
        return list;
    }
}
