package com.GraminSchool.service;

import com.GraminSchool.entity.Subject;
import com.GraminSchool.models.SubjectResponse;
import com.GraminSchool.repo.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    public Subject createSubject(SubjectResponse request){
        Subject subject=Subject.builder()
                .name(request.getName())
                .description(request.getDescription())
                .InstructorName(request.getInstructorName())
                .image(request.getImage())
                .TotalVisit(0)
                .enable(request.isEnable())
                .Other(request.getOther())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return subjectRepository.save(subject);
    }




}
