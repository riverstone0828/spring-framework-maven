package co.kr.abacus.base.api.sample.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import co.kr.abacus.base.api.sample.dto.ValidatorSampleDTO;
import co.kr.abacus.base.common.tlo.filter.TloHttpLoggingFilter;
import co.kr.abacus.base.common.tlo.TloLoggerWriter;

@WebMvcTest(controllers = ValidatorSampleController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TloHttpLoggingFilter.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TloLoggerWriter.class)
})
class ValidatorSampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void methodArgumentValidationTest() throws Exception {
        ValidatorSampleDTO dto = new ValidatorSampleDTO();
        dto.setName(""); // 빈 문자열로 유효성 검사 실패 유도

        MvcResult result = mockMvc.perform(post("/validator/method-argument-validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andReturn();

        System.out.println("Response Content: " + result.getResponse().getContentAsString());
    }
}