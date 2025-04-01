package co.kr.abacus.base.api.sample.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import co.kr.abacus.base.api.sample.dto.TLOSampleDTO;

@WebMvcTest(TLOSampleController.class)
class TLOSampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void setLogKeyHeaderTest() throws Exception {
        mockMvc.perform(post("/tlo/set-log-key-header")
                .header("x-logkey", "test-log-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(20000000));
    }

    @Test
    void writeMeTloTest() throws Exception {
        mockMvc.perform(post("/tlo/write-me-tlo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(20000000));
    }

    @Test
    void dontWriteMeTloTest() throws Exception {
        mockMvc.perform(post("/tlo/dont-write-me-tlo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(20000000));
    }

    @Test
    void createSessionTest() throws Exception {
        TLOSampleDTO dto = new TLOSampleDTO();
        dto.setUserId("test-user");

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/tlo/create-session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"test-user\"}")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(20000000));

        assertEquals("test-user", session.getAttribute("userId"));
    }

    @Test
    void resultCodeFailTest() throws Exception {
        mockMvc.perform(post("/tlo/result-code-fail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(20000000));
    }
}