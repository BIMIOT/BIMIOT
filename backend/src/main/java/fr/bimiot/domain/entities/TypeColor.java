package fr.bimiot.domain.entities;

import java.util.ArrayList;

public class TypeColor {
    private final SensorType type;
    private final ArrayList<String> colors = new ArrayList<>();
    private final ArrayList<Float> values = new ArrayList<>();

    public TypeColor(SensorType type) {
        this.type = type;
    }

    public SensorType getType() {
        return type;
    }
}
