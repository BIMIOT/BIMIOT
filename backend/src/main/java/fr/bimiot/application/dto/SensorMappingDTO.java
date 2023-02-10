package fr.bimiot.application.dto;

public class SensorMappingDTO {

    private String IFCSensorId;

    private String DataSetSensorId;

    public String getIFCSensorId() {
        return IFCSensorId;
    }

    public void setIFCSensorId(String IFCSensorId) {
        this.IFCSensorId = IFCSensorId;
    }

    public String getDataSetSensorId() {
        return DataSetSensorId;
    }

    public void setDataSetSensorId(String dataSetSensorId) {
        DataSetSensorId = dataSetSensorId;
    }
}
