package fr.bimiot.domain.entities;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Project{
    private String id;
    private String name;
    private String ifcFilename;
    private String datasetFilename;
    private byte[] ifcFile;
    private Map<SensorType, List<SensorColor>> sensorColors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIfcFilename() {
        return ifcFilename;
    }

    public void setIfcFilename(String ifcFilename) {
        this.ifcFilename = ifcFilename;
    }

    public String getDatasetFilename() {
        return datasetFilename;
    }

    public void setDatasetFilename(String datasetFilename) {
        this.datasetFilename = datasetFilename;
    }

    public byte[] getIfcFile() {
        return ifcFile;
    }

    public void setIfcFile(byte[] ifcFile) {
        this.ifcFile = ifcFile;
    }

    public Map<SensorType, List<SensorColor>> getSensorColors() {
        return sensorColors;
    }

    public void setSensorColors(Map<SensorType, List<SensorColor>> sensorColors) {
        this.sensorColors = sensorColors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return name.equals(project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
