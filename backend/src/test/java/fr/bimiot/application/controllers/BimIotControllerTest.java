package fr.bimiot.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.use_cases.ManageData;
import fr.bimiot.fixtures.RoomFixture;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BimIotController.class)
class BimIotControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ManageData manageData;

    @Test
    void sendData() throws Exception {
        var data = new Data("id", "type", "10", null);
        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(data);
        var dataCaptor = ArgumentCaptor.forClass(Data.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/sendData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(manageData).execute(dataCaptor.capture());
        assertEquals("id", dataCaptor.getValue().getId());
        assertEquals("type", dataCaptor.getValue().getType());
        assertEquals("10", dataCaptor.getValue().getValue());
        assertNull(dataCaptor.getValue().getTimestamp());
    }

    @Test
    void createMapping() throws Exception {
        var room = RoomFixture.aCompleteRoom();
        var rooms = Lists.newArrayList(room);
        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(rooms);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/bimiot/mapping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(manageData, times(1)).setRoomListDTO(rooms);
    }

    @Test
    void resetValues() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bimiot/reset")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(manageData, times(1)).resetRoomListDTO();
    }
}