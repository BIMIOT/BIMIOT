package fr.bimiot.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bimiot.application.dtos.SensorColorApiMap;
import fr.bimiot.domain.use_cases.*;
import fr.bimiot.fixtures.ProjectFixture;
import fr.bimiot.fixtures.SensorColorApiMapFixture;
import fr.bimiot.fixtures.SensorColorMapFixture;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CreateProject createProject;
    @MockBean
    DeleteProject deleteProject;
    @MockBean
    ManageSimulation manageSimulation;
    @MockBean
    ManageData manageData;
    @MockBean
    UpdateSensorsColors updateSensorsColors;
    @MockBean
    GetSensorColorMap getSensorColorMap;
    @MockBean
    GetAllProjects getAllProjects;

    @Test
    void updateProjectColors() throws Exception {
        var project = ProjectFixture.aCompleteProject();
        var map = SensorColorMapFixture.sensorTypeListMapDomain();
        var mapRequest = SensorColorApiMapFixture.sensorTypeListMapApi();
        var apiRequest = new SensorColorApiMap();
        apiRequest.setSensorColorApis(mapRequest);
        BDDMockito.doReturn(project).when(updateSensorsColors).execute(project.getName(), map);
        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(apiRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/projects/colors/{projectName}", project.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(project.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(project.getName())));
    }
}