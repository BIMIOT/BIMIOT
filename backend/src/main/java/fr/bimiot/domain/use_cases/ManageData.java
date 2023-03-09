package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.*;
import fr.bimiot.simulator.ConverterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;

@Component
public class ManageData {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private List<Room> roomListDTO;
    private Map<SensorType, List<SensorColor>> sensorTypeListMap;

    public void execute(Data data) {
        var optData = getWebSocketData(data);
        if (optData.isPresent()) {
            var event = new ConverterEvent(this, optData.get());
            applicationEventPublisher.publishEvent(event);
        }
    }

    private Optional<WebSocketData> getWebSocketData(Data data) {
        // Check if type received is valid
        if (!isTypeValid(data.getType())) {
            return Optional.empty();
        }

        // If type == END, then it's the end of the simulation
        if(SensorType.END.name().equals(data.getType())){
            return Optional.of(new WebSocketData("0", data.getValue(), "0", data.getType(), "0","0"));
        }

        if (data.getValue() == null) {
            return Optional.empty();
        }

        var found = false;
        String ifcID = null;
        for (var room : roomListDTO) {
            var nb = 0;
            var sum = 0f;
            for (var sensor : room.getSensors()) {
                if (sensor.getSensorDataSetId().equals(data.getId()) && SensorType.valueOf(data.getType()).equals(sensor.getType())) {
                    // Check if the value sent is correct
                    try {
                        Float.parseFloat(data.getValue());
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }

                    sensor.setValue(data.getValue());
                    ifcID = sensor.getSensorIFCid();
                    found = true;
                }
                if (SensorType.valueOf(data.getType()).equals(sensor.getType()) && sensor.getValue() != null) {
                    nb++;
                    sum += Float.parseFloat(sensor.getValue());
                }
            }
            if (found) {
                return Optional.of(new WebSocketData(ifcID, data.getValue(), room.getRoomId(), data.getType(),
                        getMatchingColor(SensorType.valueOf(data.getType()), sum / nb), new DecimalFormat("#.#").format(sum / nb)));
            }
        }

        // Sensor ID not found
        return Optional.empty();
    }

    private boolean isTypeValid(String type) {
        for (SensorType t : SensorType.values()) {
            if (t.name().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private String getMatchingColor(SensorType sensorType, Float value) {
        var list = sensorTypeListMap.get(sensorType);
        return list.stream()
                .filter(sensorColor -> sensorColor.threshold() > value)
                .sorted((s1, s2) -> (int) (s1.threshold() - s2.threshold()))
                .toList()
                .get(0)
                .colorCode();
    }

    public void setRoomListDTO(List<Room> roomListDTO) {
        this.roomListDTO = roomListDTO;
    }

    public void setSensorTypeListMap(Map<SensorType, List<SensorColor>> sensorTypeListMap) {
        this.sensorTypeListMap = sensorTypeListMap;
    }

    public void resetRoomListDTO() {
        for (var r : this.roomListDTO) {
            for (var s : r.getSensors()) {
                s.setValue(null);
            }
        }
    }

    public void sendAllRoomsColors() {
        for (var r : this.roomListDTO) {
            var sums = new HashMap<SensorType, SumCalculator>();
            for (SensorType t : SensorType.values()) {
                sums.put(t, new SumCalculator());
            }
            for (var sensor : r.getSensors()) {
                if (sensor.getValue() != null) {
                    sums.get(sensor.getType()).addValue(Float.parseFloat(sensor.getValue()), sensor);
                }
            }
            for (SensorType t : SensorType.values()) {
                var sumCalc = sums.get(t);
                var sum = sumCalc.getSum();
                if (sum.isPresent()) {
                    var firstSensor = sumCalc.getFirstSensor();
                    var websocketdata = new WebSocketData(firstSensor.getSensorIFCid(), firstSensor.getValue(), r.getRoomId(),
                            t.toString(), getMatchingColor(t, sum.get()), new DecimalFormat("#.#").format(sum.get()));
                    var event = new ConverterEvent(this, websocketdata);
                    applicationEventPublisher.publishEvent(event);
                }
            }
        }
    }
}
