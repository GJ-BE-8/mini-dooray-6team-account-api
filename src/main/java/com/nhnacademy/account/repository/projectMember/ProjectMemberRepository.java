package com.nhnacademy.account.repository.projectMember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ProjectMemberRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String HASH_NAME = "member-projects:";

    // 넣어주고
    public List<Long> addMemberProjects(String memberId, long projectId) {
        Object object = redisTemplate.opsForHash().get(HASH_NAME, memberId);

        List<Long> list = (object != null) ?  (List<Long>) object : new ArrayList<>();
        list.add(projectId);

        redisTemplate.opsForHash().put(HASH_NAME,memberId,list);
        return list;
    }

    // 멤버가 가진 프로젝트들 가져오기
    public List<Long> getMemberProjects(String memberId) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HASH_NAME);
        List<Long> list = new ArrayList<>(entries.size());
        for (Object o : entries.keySet()) {
            if (o.equals(memberId)) {
                list = (List<Long>) entries.get(memberId);
            }
        }
        return list;
    }

    // 삭제 memberId, projectId를 받아야함
    public void deleteMemberProject(String memberId,long projectId) {
        List<Long> memberProjects = getMemberProjects(memberId);

        for (Long memberProject : memberProjects) {
            if (memberProject.equals(projectId)) {
                memberProjects.remove(projectId);
                redisTemplate.opsForHash().put(HASH_NAME,memberId,memberProjects);
            }
        }
    }
}
