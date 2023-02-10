package fr.bimiot.domain.use_cases;

import fr.bimiot.application.dto.RoomDTO;
import fr.bimiot.domain.entities.Data;
import org.springframework.stereotype.Component;

@Component
public class ManageData {
    private RoomDTO[] roomListDTO;

    public String execute(Data data) {
        for (var room : roomListDTO) {
            for (var sensor : room.getSensorMappingDTO()) {
                if (sensor.getDataSetSensorId().equals(data.getId())) {
                    return room.getRoomId() + ", " + data.getType() + ", " + data.getValue();
                    // TODO : send data in websocket
                }
            }
        }
        return ""; // TODO : Handle error when sensor not found
    }

    public void setRoomListDTO(RoomDTO[] roomListDTO) {
        this.roomListDTO = roomListDTO;
    }
}
