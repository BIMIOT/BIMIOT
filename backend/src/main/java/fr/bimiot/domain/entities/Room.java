package fr.bimiot.domain.entities;

import java.util.Objects;
import java.util.Set;

public class Room {
    private String roomId;
    private Set<Sensor> sensors;

    public Room() {
    }

    public Room(String roomId, Set<Sensor> sensors) {
        this.roomId = roomId;
        this.sensors = sensors;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomId, room.roomId) && Objects.equals(sensors, room.sensors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, sensors);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", sensors=" + sensors +
                '}';
    }
}