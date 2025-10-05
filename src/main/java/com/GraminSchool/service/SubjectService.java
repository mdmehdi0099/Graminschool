package com.GraminSchool.service;

import com.GraminSchool.entity.Courses;
import com.GraminSchool.entity.Subject;
import com.GraminSchool.models.SubjectResponse;
import com.GraminSchool.repo.CourseRepository;
import com.GraminSchool.repo.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    public ResponseEntity<?> saveSubject(SubjectResponse request) {
        // 1️⃣ Fetch the course by ID
        Courses course = courseRepository.findById(request.getCourse_id())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + request.getCourse_id()));

        // 2️⃣ Create the subject and set course reference
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        subject.setInstructorName(request.getInstructorName());
        subject.setImage(request.getImage());
        subject.setTotalVisit(0);
        subject.setEnable(true);
        subject.setEnable(request.isEnable());
        subject.setCourse(course); // ✅ Set the actual Course object, not just ID

        // 3️⃣ Save subject
        subjectRepository.save(subject);

        return ResponseEntity.ok("Subject saved successfully!");
    }





}
