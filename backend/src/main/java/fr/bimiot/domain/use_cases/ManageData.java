package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManageData {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private List<Room> roomListDTO;
    private TypesColors typesColors;

    public WebSocketData execute(Data data) {
        var found = false;
        String ifcID = null;
        for (var room : roomListDTO) {
            var nb = 0;
            var sum = 0;
            for (var sensor : room.getSensors()) {
                if (sensor.getSensorDataSetId().equals(data.getId())) {
                    sensor.setValue(data.getValue());
                    ifcID = sensor.getSensorIFCid();
                    found = true;
                }
                if (SensorType.valueOf(data.getType()).equals(sensor.getType())) {
                    nb++;
                    sum+=Float.parseFloat(sensor.getValue());
                }
            }
            if (found) {
                return new WebSocketData(ifcID, data.getValue(), room.getRoomId(), data.getType(), "red"); // TODO : get color from average (sum / nb)
            }
        }
        return new WebSocketData("0","0","0","0", "0"); // TODO : Handle error when sensor not found
    }

    public void setRoomListDTO(List<Room> roomListDTO) {
        this.roomListDTO = roomListDTO;
    }
}
