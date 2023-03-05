package fr.bimiot.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bimiot.application.dtos.SensorColorApiMap;
import fr.bimiot.domain.use_cases.*;
import fr.bimiot.fixtures.ProjectFixture;
import fr.bimiot.fixtures.SensorColorApiMapFixture;
import fr.bimiot.fixtures.SensorColorMapFixture;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    void getAllProjects() throws Exception {
        var project = ProjectFixture.aCompleteProject();
        BDDMockito.doReturn(Lists.newArrayList(project)).when(getAllProjects).execute();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bimiot/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Is.is(project.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ifcFilename", Is.is(project.getIfcFilename())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].datasetFilename", Is.is(project.getDatasetFilename())));
    }

    @Test
    void getAllProjects_shouldReturnEmptyList() throws Exception {
        BDDMockito.doReturn(Lists.newArrayList()).when(getAllProjects).execute();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bimiot/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void getSensorColorMap() throws Exception {
        var project = ProjectFixture.aCompleteProject();
        var sensorColorMap = SensorColorMapFixture.sensorTypeListMapDomain();
        BDDMockito.doReturn(sensorColorMap).when(getSensorColorMap).execute(project.getName());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bimiot/projects/colors/{projectName}", project.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.TEMPERATURE", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.LIGHT", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.CO2", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.HUMIDITY", Matchers.notNullValue()));
    }

    @Test
    void deleteProject() throws Exception {
        var projectName = ProjectFixture.aCompleteProject().getName();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bimiot/projects/{projectName}", projectName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(deleteProject, times(1)).execute(projectName);
    }

    @Test
    void createProject() throws Exception {
        var ifcFile = new MockMultipartFile("ifc", "test.ifc", "application/octet-stream", "test".getBytes());
        var datasetFile = new MockMultipartFile("dataset", "test.json", "application/json", "test".getBytes());
        BDDMockito.doReturn("ID").when(createProject).execute(ProjectFixture.aCompleteProject(), datasetFile);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/bimiot/projects")
                        .file(ifcFile)
                        .file(datasetFile)
                        .param("name", ProjectFixture.aCompleteProject().getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("ID"));
    }
}