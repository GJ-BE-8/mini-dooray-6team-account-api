package com.nhnacademy.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.account.config.SecurityConfig;
import com.nhnacademy.account.entity.projectMember.MemberProjectRequest;
import com.nhnacademy.account.repository.projectMember.ProjectMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(MemberProjectController.class)
@Import(SecurityConfig.class)
class MemberProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectMemberRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(repository);
    }

    @Test
    @WithMockUser
    void testAddMemberProject() throws Exception {
        List<Long> mockResponse = Arrays.asList(1L, 2L);
        Mockito.when(repository.addMemberProjects(anyString(), anyLong())).thenReturn(mockResponse);

        MemberProjectRequest request = new MemberProjectRequest("member1", 1L);

        mockMvc.perform(post("/member-projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("[1,2]"));

        Mockito.verify(repository).addMemberProjects("member1", 1L);
    }

    @Test
    void testDeleteMemberProject() throws Exception {
        Mockito.doNothing().when(repository).deleteMemberProject(Mockito.anyString(), Mockito.anyLong());

        MemberProjectRequest request = new MemberProjectRequest("member1", 1L);

        mockMvc.perform(delete("/member-projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(repository).deleteMemberProject("member1", 1L);
    }


    @Test
    void testGetMemberProject() throws Exception {
        List<Long> mockResponse = Arrays.asList(1L, 2L);
        Mockito.when(repository.getMemberProjects("member1")).thenReturn(mockResponse);

        mockMvc.perform(get("/member-projects/member1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[1,2]"));

        Mockito.verify(repository).getMemberProjects("member1");
    }
}
