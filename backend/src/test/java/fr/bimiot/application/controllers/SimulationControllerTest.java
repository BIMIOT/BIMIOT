package fr.bimiot.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bimiot.application.dtos.UpdateSimulationSettingsRequest;
import fr.bimiot.domain.use_cases.LoadIfcFile;
import fr.bimiot.domain.use_cases.simulation.*;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SimulationController.class)
class SimulationControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    LoadIfcFile loadIfcFile;
    @MockBean
    StartSimulation startSimulation;
    @MockBean
    StopSimulation stopSimulation;
    @MockBean
    PauseSimulation pauseSimulation;
    @MockBean
    UpdateSimulationSettings updateSimulationSettings;

    @MockBean
    GetSimulationSettings getSimulationSettings;

    @Test
    void startSimulation_shouldCallExecuteOfStartSimulation() throws Exception {
        var projectName = ProjectFixture.aCompleteProject().getName();
        var projectNameCaptor = ArgumentCaptor.forClass(String.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/simulations/start/{projectName}", projectName)
                .contentType(MediaType.APPLICATION_JSON));

        verify(startSimulation, times(1)).execute(projectName);
        verify(startSimulation).execute(projectNameCaptor.capture());
        assertEquals(projectName, projectNameCaptor.getValue());
    }

    @Test
    void stopSimulation_shouldCallExecuteOfStopSimulation() throws Exception {
        var projectName = ProjectFixture.aCompleteProject().getName();
        var projectNameCaptor = ArgumentCaptor.forClass(String.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/simulations/stop/{projectName}", projectName)
                .contentType(MediaType.APPLICATION_JSON));
        verify(stopSimulation, times(1)).execute(projectName);
        verify(stopSimulation).execute(projectNameCaptor.capture());
        assertEquals(projectName, projectNameCaptor.getValue());
    }

    @Test
    void pauseSimulation_shouldCallExecuteOfPauseSimulation() throws Exception {
        var projectName = ProjectFixture.aCompleteProject().getName();
        var projectNameCaptor = ArgumentCaptor.forClass(String.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/simulations/pause/{projectName}", projectName)
                .contentType(MediaType.APPLICATION_JSON));
        verify(pauseSimulation, times(1)).execute(projectName);
        verify(pauseSimulation).execute(projectNameCaptor.capture());
        assertEquals(projectName, projectNameCaptor.getValue());
    }

    @Test
    void updateSettings_shouldCallExecuteOfUpdateSimulationSettings() throws Exception {
        var projectName = ProjectFixture.aCompleteProject().getName();
        var hostCaptor = ArgumentCaptor.forClass(String.class);
        var portCaptor = ArgumentCaptor.forClass(Integer.class);
        UpdateSimulationSettingsRequest updateSimulationSettingsRequest = new UpdateSimulationSettingsRequest();
        updateSimulationSettingsRequest.setHost("localhost");
        updateSimulationSettingsRequest.setPort(3000);
        ObjectMapper mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(updateSimulationSettingsRequest);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/simulations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(updateSimulationSettings).execute(hostCaptor.capture(), portCaptor.capture());
        assertEquals(updateSimulationSettingsRequest.getHost(), hostCaptor.getValue());
        assertEquals(updateSimulationSettingsRequest.getPort(), portCaptor.getValue());
    }

    @Test
    void loadFile() throws Exception {
        var projectName = ProjectFixture.aCompleteProject().getName();
        BDDMockito.doReturn("Hello".getBytes()).when(loadIfcFile).execute(projectName);
        var projectNameCaptor = ArgumentCaptor.forClass(String.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bimiot/simulations/files/{projectName}", projectName)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes("Hello".getBytes()));
        verify(loadIfcFile).execute(projectNameCaptor.capture());
        assertEquals(projectName, projectNameCaptor.getValue());
    }
}
