package fr.bimiot.domain.entities;

import java.sql.Timestamp;

public class Data {
    private String id;
    private String type;
    private String value;
    private Timestamp timestamp;

    public Data(String id, String type, String value, Timestamp timestamp) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Data{" +
                "dataSetID='" + id + '\'' +
                ", sensorType='" + type + '\'' +
                ", value='" + value + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
