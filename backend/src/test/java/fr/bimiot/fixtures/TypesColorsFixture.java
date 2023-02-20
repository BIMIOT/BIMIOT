package fr.bimiot.fixtures;

import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.entities.TypeColor;
import fr.bimiot.domain.entities.TypesColors;

import java.util.HashMap;
import java.util.List;

public class TypesColorsFixture {
    public static TypesColors allSensors() {
        var typesColors = new TypesColors();
        var map = new HashMap<SensorType, TypeColor>();
        map.put(SensorType.TEMPERATURE, aSimpleTypeColor());
        map.put(SensorType.CO2, aSimpleTypeColor());
        map.put(SensorType.LIGHT, aSimpleTypeColor());
        map.put(SensorType.HUMIDITY, aSimpleTypeColor());
        typesColors.setTypesColor(map);
        return typesColors;
    }

    private static TypeColor aSimpleTypeColor() {
        var typeColor = new TypeColor();
        typeColor.setColors(List.of("#219ebc", "#ffb703"));
        typeColor.setValues(List.of(10f));
        return typeColor;
    }
}
