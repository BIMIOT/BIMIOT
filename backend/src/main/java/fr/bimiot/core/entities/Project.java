package fr.bimiot.core.entities;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Project{
    private String id;
    private String name;
    private MultipartFile ifc;
    private MultipartFile dataset;
    private Map<SensorType, List<SensorColor>> sensorColors;

    public Project() {
    }

    public Project(String id, String name, MultipartFile ifc, MultipartFile dataset) {
        this.id = id;
        this.name = name;
        this.ifc = ifc;
        this.dataset = dataset;
    }

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

    public MultipartFile getIfc() {
        return ifc;
    }

    public void setIfc(MultipartFile ifc) {
        this.ifc = ifc;
    }

    public MultipartFile getDataset() {
        return dataset;
    }

    public void setDataset(MultipartFile dataset) {
        this.dataset = dataset;
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
