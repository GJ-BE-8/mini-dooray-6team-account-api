package com.nhnacademy.account.repository.projectMember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class ProjectMemberRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String HASH_NAME = "member-projects:";

    // 넣어주고
    // 멤버 프로젝트 추가
    public List<Long> addMemberProjects(String memberId, long projectId) {
        Object object = redisTemplate.opsForHash().get(HASH_NAME, memberId);

        List<Long> list = (object != null) ? (List<Long>) object : new ArrayList<>();

        // 중복 확인
        if (list.contains(projectId)) {
            throw new IllegalArgumentException("Project ID " + projectId + " already exists for member " + memberId);
        }

        list.add(projectId);

        redisTemplate.opsForHash().put(HASH_NAME, memberId, list);
        return list;
    }


    // 멤버가 가진 프로젝트들 가져오기
    public List<Long> getMemberProjects(String memberId) {
        Object object = redisTemplate.opsForHash().get(HASH_NAME, memberId);
        return (object != null) ? (List<Long>) object : new ArrayList<>();
    }

    // 삭제 memberId, projectId를 받아야함
    public void deleteMemberProject(String memberId, long projectId) {
        List<Long> memberProjects = getMemberProjects(memberId);
        Iterator<Long> iterator = memberProjects.iterator();

        while (iterator.hasNext()) {
            Long memberProject = iterator.next();
            if (memberProject.equals(projectId)) {
                iterator.remove(); // Iterator를 통해 안전하게 삭제
            }
        }

        redisTemplate.opsForHash().put(HASH_NAME, memberId, memberProjects);
    }

}
