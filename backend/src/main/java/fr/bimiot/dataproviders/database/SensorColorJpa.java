package fr.bimiot.dataproviders.database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorColorJpa {
    private String colorCode;
    private float rangeStart;
    private float rangeEnd;
}
