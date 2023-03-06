package fr.bimiot.domain.use_cases.providers;

import org.springframework.web.multipart.MultipartFile;

public interface SimulatorProvider {
    void createSimulation(String projectName, MultipartFile file);

    void deleteSimulation(String projectName);

    void start(String projectName);

    void stop(String projectName);

    void pause(String projectName);

    void updateAddress(String simulatorAddress, Integer simulatorPort);

    String getCompleteSimulatorAddress();
}
