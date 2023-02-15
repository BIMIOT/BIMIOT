package fr.bimiot.domain.entities;

import java.util.ArrayList;
import java.util.Objects;

public class TypeColor {
    private ArrayList<String> colors;
    private ArrayList<Float> values;

    public TypeColor(ArrayList<String> colors, ArrayList<Float> values) {
        this.colors = colors;
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeColor typeColor = (TypeColor) o;
        return Objects.equals(colors, typeColor.colors) && Objects.equals(values, typeColor.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colors, values);
    }

    @Override
    public String toString() {
        return "TypeColor{" +
                "colors=" + colors +
                ", values=" + values +
                '}';
    }
}
