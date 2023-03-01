package fr.bimiot.domain.entities;

public record WebSocketData(String sensorIfcID, String value, String roomIfcID, String type, String color,String averageValue) {

}
