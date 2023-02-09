package fr.bimiot.application.dto;

public class RoomDTO {
    private String roomId;

    private SensorMappingDTO[] sensorMappingDTO;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public SensorMappingDTO[] getSensorMappingDTO() {
        return sensorMappingDTO;
    }

    public void setSensorMappingDTO(SensorMappingDTO[] sensorMappingDTO) {
        this.sensorMappingDTO = sensorMappingDTO;
    }
}
