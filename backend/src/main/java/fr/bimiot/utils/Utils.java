package fr.bimiot.utils;

public class Utils {
    public static boolean isNotValidProjectName(String projectName) {
        return projectName == null || projectName.isEmpty() || projectName.isBlank();
    }
}
