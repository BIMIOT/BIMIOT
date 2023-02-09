package fr.bimiot.application;

public class ProjectDirectoryApi {
    private String directoryName;
    private String ifcFile;
    private String datasetFile;

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getIfcFile() {
        return ifcFile;
    }

    public void setIfcFile(String ifcFile) {
        this.ifcFile = ifcFile;
    }

    public String getDatasetFile() {
        return datasetFile;
    }

    public void setDatasetFile(String datasetFile) {
        this.datasetFile = datasetFile;
    }
}
