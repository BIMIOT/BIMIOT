package fr.bimiot.application;

import org.springframework.web.multipart.MultipartFile;

public class ProjectDirectoryApi {
    private String directoryName;
    private MultipartFile ifcFile;
    private MultipartFile datasetFile;

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public MultipartFile getIfcFile() {
        return ifcFile;
    }

    public void setIfcFile(MultipartFile ifcFile) {
        this.ifcFile = ifcFile;
    }

    public MultipartFile getDatasetFile() {
        return datasetFile;
    }

    public void setDatasetFile(MultipartFile datasetFile) {
        this.datasetFile = datasetFile;
    }
}
