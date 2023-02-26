package fr.bimiot.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Project{
    private String id;
    private String name;
    private byte[] ifc;
    private byte[] dataset;
    private Map<SensorType, List<SensorColor>> sensorColors;

    public Project() {
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
