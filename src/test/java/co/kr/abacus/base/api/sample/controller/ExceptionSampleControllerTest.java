package co.kr.abacus.base.api.sample.controller;

import co.kr.abacus.base.api.sample.dto.ExceptionSampleDTO;
import co.kr.abacus.base.api.sample.dto.ExceptionSampleReqDTO;
import co.kr.abacus.base.common.response.APIResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExceptionSampleController.class)
class ExceptionSampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void internalServerErrorTest() throws Exception {
        mockMvc.perform(get("/exception/internal-server-error"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void unauthorizedTest() throws Exception {
        mockMvc.perform(get("/exception/unauthorized"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void forbiddenTest() throws Exception {
        mockMvc.perform(get("/exception/forbidden"))
                .andExpect(status().isForbidden());
    }

    @Test
    void serverErrorTest() throws Exception {
        mockMvc.perform(get("/exception/server-error"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void missingParameterTest() throws Exception {
        mockMvc.perform(get("/exception/missing-param"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void handlerValidationTest() throws Exception {
        mockMvc.perform(get("/exception/handler-validation")
                .param("value", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void methodArgumentValidationTest() throws Exception {
        ExceptionSampleReqDTO dto = new ExceptionSampleReqDTO();
        dto.setName(""); // 빈 문자열로 유효성 검사 실패 유도

        mockMvc.perform(post("/exception/method-argument-validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bindExceptionTest() throws Exception {
        ExceptionSampleDTO dto = new ExceptionSampleDTO();
        dto.setId(0L); // 1 미만의 값으로 유효성 검사 실패 유도
        dto.setName(""); // 빈 문자열로 유효성 검사 실패 유도

        mockMvc.perform(post("/exception/bind-exception")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":0,\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bindExceptionTest2() throws Exception {
        ExceptionSampleDTO dto = new ExceptionSampleDTO();
        dto.setId(0L);
        dto.setName("");

        mockMvc.perform(post("/exception/bind-exception2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":0,\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}