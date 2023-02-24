package fr.bimiot.core.use_cases;

import fr.bimiot.core.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ManageData {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private List<Room> roomListDTO;
    private Map<SensorType, List<SensorColor>> sensorTypeListMap;

    public WebSocketData execute(Data data) {
        var found = false;
        String ifcID = null;
        for (var room : roomListDTO) {
            var nb = 0;
            var sum = 0f;
            for (var sensor : room.getSensors()) {
                if (sensor.getSensorDataSetId().equals(data.getId())) {
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
                return new WebSocketData(ifcID, data.getValue(), room.getRoomId(), data.getType(),
                        getMatchingColor(SensorType.valueOf(data.getType()), sum / nb));
            }
        }
        return new WebSocketData("0", "0", "0", "0", "0"); // TODO : Handle error when sensor not found
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
}
