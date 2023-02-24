package fr.bimiot.entrypoints.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bimiot.entrypoints.dtos.SensorColorApiMap;
import fr.bimiot.entrypoints.dtos.SensorTypeApi;
import fr.bimiot.core.use_cases.*;
import fr.bimiot.fixtures.ProjectFixture;
import fr.bimiot.fixtures.SensorColorApiMapFixture;
import fr.bimiot.fixtures.SensorColorMapFixture;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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

    @ParameterizedTest
    @EnumSource(SensorTypeApi.class)
    void getSensorColorMap_shouldReturnSensorColorApiMap(SensorTypeApi typeApi) throws Exception {
        //  Given
        String projectName = ProjectFixture.aCompleteProject().getName();
        var executeResult = SensorColorMapFixture.sensorTypeListMapDomain();
        Mockito.when(getSensorColorMap.execute(projectName)).thenReturn(executeResult);
        var expectedMap = SensorColorApiMapFixture.sensorTypeListMapEntrypoint();
        //  When / Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bimiot/projects/colors/{projectName}", projectName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$." + typeApi.name() + "[0].colorCode", Is.is("#8ecae6")))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + typeApi.name() + "[0].threshold", Is.is(10.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + typeApi.name() + "[1].colorCode", Is.is("#219ebc")))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + typeApi.name() + "[1].threshold", Is.is(20.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + typeApi.name() + "[2].colorCode", Is.is("#023047")))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + typeApi.name() + "[2].threshold", Is.is(30.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + typeApi.name() + "[3].colorCode", Is.is("#ffb703")))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + typeApi.name() + "[3].threshold", Is.is("Infinity")));
    }
}