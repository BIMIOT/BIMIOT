package fr.bimiot.domain.entities;

import java.util.Objects;

public class Sensor {
    private String sensorDataSetId;
    private String sensorIFCid;
    private SensorType type;
    private String value;

    public Sensor(String sensorDataSetId, String sensorIFCid, SensorType type, String value) {
        this.sensorDataSetId = sensorDataSetId;
        this.sensorIFCid = sensorIFCid;
        this.type = type;
        this.value = value;
    }

    public Sensor() {}

    public String getSensorDataSetId() {
        return sensorDataSetId;
    }

    public String getSensorIFCid() {
        return sensorIFCid;
    }

    public SensorType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(sensorDataSetId, sensor.sensorDataSetId) && Objects.equals(sensorIFCid, sensor.sensorIFCid) && type == sensor.type && Objects.equals(value, sensor.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorDataSetId, sensorIFCid, type, value);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorDataSetId='" + sensorDataSetId + '\'' +
                ", sensorIFCid='" + sensorIFCid + '\'' +
                ", type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}