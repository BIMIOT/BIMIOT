package fr.bimiot.fixtures;

import fr.bimiot.entrypoints.dtos.SensorColorApi;
import fr.bimiot.entrypoints.dtos.SensorTypeApi;

import java.util.*;

public class SensorColorApiMapFixture {
    public static Map<SensorTypeApi, List<SensorColorApi>> sensorTypeListMapEntrypoint() {
        var map = new HashMap<SensorTypeApi, List<SensorColorApi>>();
        Arrays.stream(SensorTypeApi.values())
                .forEach(sensorTypeApi -> map.put(sensorTypeApi, aCompletePaletteSensorColorApi()));
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
