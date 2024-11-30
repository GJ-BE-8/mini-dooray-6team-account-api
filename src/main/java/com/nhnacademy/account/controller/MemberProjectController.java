package com.nhnacademy.account.controller;

import com.nhnacademy.account.entity.projectMember.MemberProjectRequest;
import com.nhnacademy.account.repository.projectMember.ProjectMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member-projects")
public class MemberProjectController {

    @Autowired
    private ProjectMemberRepository repository;

    // add
    @PostMapping
    public List<Long> addMemberProject(@RequestBody MemberProjectRequest request){
       return repository.addMemberProjects(request.getMemberId(),request.getProjectId());
    }

    //delete
    @DeleteMapping
    public ResponseEntity deleteMemberProject(@RequestBody MemberProjectRequest request){
        repository.deleteMemberProject(request.getMemberId(),request.getProjectId());
        return ResponseEntity.ok().build();
    }

    //get
    @GetMapping("/{memberId}")
    public List<Long> getMemberProject(@PathVariable String memberId){
        return repository.getMemberProjects(memberId);
    }
}
