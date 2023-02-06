package fr.bimiot.domain.entities;

public enum SensorType {
    TEMPERATURE("temp"),
    HUMIDITY("hum"),
    CO2("co2"),
    LIGHT("lum");
    private final String type;

    SensorType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}