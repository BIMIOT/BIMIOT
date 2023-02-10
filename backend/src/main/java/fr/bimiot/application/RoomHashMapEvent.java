package fr.bimiot.application;

import fr.bimiot.domain.entities.Room;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class RoomHashMapEvent extends ApplicationEvent {
    private String rooms;

    public RoomHashMapEvent(Object source, String rooms) {
        super(source);
        this.rooms = rooms;
    }
    public String getMessage() {
        return rooms;
    }
}