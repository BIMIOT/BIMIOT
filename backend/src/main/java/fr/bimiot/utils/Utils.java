package fr.bimiot.utils;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isNotValidProjectName(String projectName) {
        return projectName == null || projectName.isEmpty() || projectName.isBlank();
    }
}
