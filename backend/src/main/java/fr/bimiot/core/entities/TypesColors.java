package fr.bimiot.core.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TypesColors {
    private Map<SensorType, TypeColor> typesColor;

    public TypesColors() {
    }

    public TypesColors(HashMap<SensorType, TypeColor> typesColor) {
        this.typesColor = typesColor;
    }

    public Map<SensorType, TypeColor> getTypesColor() {
        return typesColor;
    }

    public void setTypesColor(Map<SensorType, TypeColor> typesColor) {
        this.typesColor = typesColor;
    }

    public String getColor(SensorType type, Float value) {
        return typesColor.get(type).getColor(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypesColors that = (TypesColors) o;
        return Objects.equals(typesColor, that.typesColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typesColor);
    }

    @Override
    public String toString() {
        return "TypesColors{" +
                "typesColor=" + typesColor +
                '}';
    }
}
