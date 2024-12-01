package com.nhnacademy.account.repository.projectMember;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectMemberRepositoryTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private ProjectMemberRepository repository;

    private static final String HASH_NAME = "member-projects:";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    @Test
    void testAddMemberProjects_NewMember() {
        String memberId = "member1";
        long projectId = 1L;

        when(hashOperations.get(HASH_NAME, memberId)).thenReturn(null);

        List<Long> result = repository.addMemberProjects(memberId, projectId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(projectId));
        verify(hashOperations).put(HASH_NAME, memberId, result);
    }

    @Test
    void testAddMemberProjects_ExistingMember() {
        String memberId = "member1";
        long projectId = 2L;

        List<Long> existingProjects = new ArrayList<>();
        existingProjects.add(1L);

        when(hashOperations.get(HASH_NAME, memberId)).thenReturn(existingProjects);

        List<Long> result = repository.addMemberProjects(memberId, projectId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(projectId));
        verify(hashOperations).put(HASH_NAME, memberId, result);
    }

    @Test
    void testAddMemberProjects_DuplicateProject() {
        String memberId = "member1";
        long projectId = 1L;

        List<Long> existingProjects = new ArrayList<>();
        existingProjects.add(projectId);

        when(hashOperations.get(HASH_NAME, memberId)).thenReturn(existingProjects);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.addMemberProjects(memberId, projectId)
        );

        assertEquals("Project ID " + projectId + " already exists for member " + memberId, exception.getMessage());
        verify(hashOperations, never()).put(anyString(), anyString(), anyList());
    }

    @Test
    void testGetMemberProjects_ExistingMember() {
        String memberId = "member1";
        List<Long> mockProjects = List.of(1L, 2L);

        when(hashOperations.get(HASH_NAME, memberId)).thenReturn(mockProjects);

        List<Long> result = repository.getMemberProjects(memberId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
    }

    @Test
    void testGetMemberProjects_NonExistingMember() {
        String memberId = "nonExistingMember";

        when(hashOperations.get(HASH_NAME, memberId)).thenReturn(null);

        List<Long> result = repository.getMemberProjects(memberId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteMemberProject_Success() {
        String memberId = "member1";
        long projectId = 1L;

        List<Long> existingProjects = new ArrayList<>();
        existingProjects.add(projectId);
        existingProjects.add(2L);

        when(hashOperations.get(HASH_NAME, memberId)).thenReturn(existingProjects);

        repository.deleteMemberProject(memberId, projectId);

        assertFalse(existingProjects.contains(projectId));
        assertEquals(1, existingProjects.size());
        verify(hashOperations).put(HASH_NAME, memberId, existingProjects);
    }

    @Test
    void testDeleteMemberProject_ProjectNotFound() {
        String memberId = "member1";
        long projectId = 3L;

        List<Long> existingProjects = new ArrayList<>();
        existingProjects.add(1L);
        existingProjects.add(2L);

        when(hashOperations.get(HASH_NAME, memberId)).thenReturn(existingProjects);

        repository.deleteMemberProject(memberId, projectId);

        assertEquals(2, existingProjects.size());
        assertFalse(existingProjects.contains(projectId));
        verify(hashOperations).put(HASH_NAME, memberId, existingProjects);
    }

    @Test
    void testDeleteMemberProject_EmptyProjects() {
        String memberId = "member1";
        long projectId = 1L;

        when(hashOperations.get(HASH_NAME, memberId)).thenReturn(new ArrayList<>());

        repository.deleteMemberProject(memberId, projectId);

        verify(hashOperations).put(HASH_NAME, memberId, new ArrayList<>());
    }
}
