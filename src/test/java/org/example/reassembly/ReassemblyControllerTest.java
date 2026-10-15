package org.example.reassembly;

import org.example.reassembly.controller.ReassemblyController;
import org.example.reassembly.service.ReassemblyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReassemblyController.class)
class ReassemblyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReassemblyService reassemblyService;

    @Test
    void reassemblesDocumentThroughHttpEndpoint() throws Exception {
        given(reassemblyService.reassemble(
                "This ;s is an exam;xample problem.;blem."
        )).willReturn("This is an example problem.");

        mockMvc.perform(post("/reassemble")
                        .contentType(TEXT_PLAIN)
                        .accept(TEXT_PLAIN)
                        .content("This ;s is an exam;xample problem.;blem."))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TEXT_PLAIN))
                .andExpect(content().string("This is an example problem."));
    }

    @Test
    void rejectsEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/reassemble")
                        .contentType(TEXT_PLAIN)
                        .accept(TEXT_PLAIN)
                        .content(""))
                .andExpect(status().isBadRequest());

        then(reassemblyService).shouldHaveNoInteractions();
    }
}