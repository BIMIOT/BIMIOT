package fr.bimiot.domain.entities;

import java.util.HashMap;
import java.util.Objects;

public class TypesColors {
    private HashMap<SensorType, TypeColor> typesColor;

    public TypesColors() {
    }

    public void setTypesColor(HashMap<SensorType, TypeColor> typesColor) {
        this.typesColor = typesColor;
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
