package fr.bimiot.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bimiot.application.dtos.SensorColorApiMap;
import fr.bimiot.domain.use_cases.*;
import fr.bimiot.fixtures.ProjectFixture;
import fr.bimiot.fixtures.SensorColorApiMapFixture;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
    GetAllProjects getAllProjects;
    @MockBean
    CreateProject createProject;
    @MockBean
    UpdateSensorsColors updateSensorsColors;
    @MockBean
    DeleteProject deleteProject;
    @MockBean
    ManageSimulation manageSimulation;
    @MockBean
    ManageData manageData;
    @MockBean
    GetSensorColorMap getSensorColorMap;

    @Test
    void getAllProjects() throws Exception {
        String projectName = ProjectFixture.aCompleteProject().getName();
        Mockito.when(getAllProjects.execute()).thenReturn(Lists.newArrayList(projectName));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bimiot/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", Is.is(projectName)));
    }

    @Test
    void updateSensorsColors_shouldReturnProjectWithNewSensorsColors() throws Exception {
        var project = ProjectFixture.aCompleteProject();
        var inputProjectName = project.getName();
        var sensorColorMapApi = new SensorColorApiMap();
        sensorColorMapApi.setSensorColorApis(SensorColorApiMapFixture.sensorTypeListMapEntrypoint());

        Mockito.when(updateSensorsColors.execute(inputProjectName, project.getSensorColors())).thenReturn(project);
        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(sensorColorMapApi);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/projects/colors/{projectName}", inputProjectName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("ProjectID")));
    }
}