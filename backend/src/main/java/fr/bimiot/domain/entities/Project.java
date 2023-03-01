package fr.bimiot.domain.entities;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

public class Project{
    private String id;
    private String name;
    private String ifcFilename;
    private String datasetFilename;
    private byte[] ifcFile;
    private byte[] datasetFile;
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
