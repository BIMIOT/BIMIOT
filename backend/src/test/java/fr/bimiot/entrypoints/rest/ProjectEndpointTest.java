package fr.bimiot.entrypoints.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bimiot.core.use_cases.*;
import fr.bimiot.entrypoints.dtos.UpdateProjectRequest;
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
@WebMvcTest(ProjectEndpoint.class)
class ProjectEndpointTest {
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
    UpdateProject updateProject;
    @MockBean
    FindAllProjects findAllProjects;

    @Test
    void findAllProjects() throws Exception {
        //  Given
        var project = ProjectFixture.aCompleteProject();
        Mockito.when(findAllProjects.execute()).thenReturn(Lists.newArrayList(project));
        //  When / Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bimiot/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Is.is(project.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Is.is(project.getName())));
    }

    @Test
    void updateProject() throws Exception {
        //  Given
        var project = ProjectFixture.aCompleteProject();
        var updateProjectRequest = new UpdateProjectRequest();
        updateProjectRequest.setName(project.getName());
        updateProjectRequest.setIfcFileContent(project.getIfc());
        updateProjectRequest.setSensorsColorsMap(SensorColorApiMapFixture.sensorTypeListMapEntrypoint());
        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(updateProjectRequest);
        Mockito.when(updateProject.execute(project)).thenReturn(project);
        //  When / Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/projects/{id}", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(project.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(project.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ifcFileContent", Is.is(Matchers.notNullValue())));
    }
}