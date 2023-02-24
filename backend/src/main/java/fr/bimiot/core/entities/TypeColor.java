package fr.bimiot.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class TypeColor {
    private List<String> colors;
    private List<Float> values;

    public TypeColor() {
    }

    public TypeColor(ArrayList<String> colors, ArrayList<Float> values) {
        this.colors = colors;
        this.values = values;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public String getColor(Float value) {
        return colors.get(IntStream.range(0, values.size())
                .filter(i -> value <= values.get(i))
                .findFirst().orElse(colors.size()-1));
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
