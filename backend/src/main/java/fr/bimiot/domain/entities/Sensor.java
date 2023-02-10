package fr.bimiot.domain.entities;

import java.util.Objects;

public class Sensor {
    private String sensorDataSetId;
    private String sensorIFCid;

    public Sensor(String sensorDataSetId, String sensorIFCid) {
        this.sensorDataSetId = sensorDataSetId;
        this.sensorIFCid = sensorIFCid;
    }

    public Sensor() {}

    public String getSensorDataSetId() {
        return sensorDataSetId;
    }

    public String getSensorIFCid() {
        return sensorIFCid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(sensorDataSetId, sensor.sensorDataSetId) && Objects.equals(sensorIFCid, sensor.sensorIFCid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorDataSetId, sensorIFCid);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorDataSetId='" + sensorDataSetId + '\'' +
                ", sensorIFCid='" + sensorIFCid + '\'' +
                '}';
    }
}