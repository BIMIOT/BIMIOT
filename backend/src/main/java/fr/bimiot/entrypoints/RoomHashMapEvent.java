package fr.bimiot.entrypoints;

import org.springframework.context.ApplicationEvent;

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