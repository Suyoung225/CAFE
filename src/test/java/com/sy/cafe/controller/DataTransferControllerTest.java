package com.sy.cafe.controller;

import com.sy.cafe.sse.DataTransferController;
import com.sy.cafe.sse.DataTransferService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataTransferController.class)
@MockBean(JpaMetamodelMappingContext.class)
class DataTransferControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    DataTransferService dataTransferService;

    @Test
    @DisplayName("SSE 연결")
    void subscribe() throws Exception{
        mvc.perform(get("/connect"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("SSE 헤더")
    void subscribeWithHeader() throws Exception{
        mvc.perform(get("/connect")
                        .header("Last-Event-ID", "OrderData_1675313587027"))
                .andExpect(status().isOk());
    }
}