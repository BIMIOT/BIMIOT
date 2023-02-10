package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.Room;
import fr.bimiot.domain.entities.WebSocketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManageData {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private List<Room> roomListDTO;

    public WebSocketData execute(Data data) {
        for (var room : roomListDTO) {
            for (var sensor : room.getSensors()) {
                if (sensor.getSensorDataSetId().equals(data.getId())) {
                    return new WebSocketData(sensor.getSensorIFCid(), data.getValue(), room.getRoomId(), data.getType());
                }
            }
        }
        return new WebSocketData("0","0","0","0"); // TODO : Handle error when sensor not found
    }

    public void setRoomListDTO(List<Room> roomListDTO) {
        this.roomListDTO = roomListDTO;
    }
}
