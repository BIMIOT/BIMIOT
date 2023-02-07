package fr.bimiot.application;

import com.fasterxml.jackson.annotation.JsonRawValue;
public class ProjectDirectoryApi {
    private String directoryName;

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
}
