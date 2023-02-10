package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.Room;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManageData {
    private List<Room> roomListDTO;

    public String execute(Data data) {
        for (var room : roomListDTO) {
            for (var sensor : room.getSensors()) {
                if (sensor.getSensorDataSetId().equals(data.getId())) {
                    return room.getRoomId() + ", " + data.getType() + ", " + data.getValue();
                    // TODO : send data in websocket
                }
            }
        }
        return ""; // TODO : Handle error when sensor not found
    }

    public void setRoomListDTO(List<Room> roomListDTO) {
        this.roomListDTO = roomListDTO;
    }
}
