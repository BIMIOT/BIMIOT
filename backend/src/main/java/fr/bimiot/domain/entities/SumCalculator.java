package fr.bimiot.domain.entities;

import java.util.Optional;

public class SumCalculator {
    private float value;
    private int nb;
    private Sensor firstSensor;

    public Optional<Float> getSum() {
        if (nb == 0) {
            return Optional.empty();
        }
        return Optional.of(value / nb);
    }

    public Sensor getFirstSensor() {
        if (firstSensor == null) {
            throw new IllegalStateException("Insert a value with a sensor first.");
        }
        return firstSensor;
    }

    public void addValue(float value, Sensor sensor) {
        if (firstSensor == null) {
            this.firstSensor = sensor;
        }
        this.value+=value;
        this.nb++;
    }
}
