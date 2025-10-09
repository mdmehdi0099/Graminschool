package com.GraminSchool.service;

import com.GraminSchool.entity.Courses;
import com.GraminSchool.entity.Subject;
import com.GraminSchool.dto.SubjectRequest;
import com.GraminSchool.repo.CourseRepository;
import com.GraminSchool.repo.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Subject saveSubject(SubjectRequest request) {
        // ✅ Check for duplicate subject name (case-insensitive)
        subjectRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
            throw new IllegalArgumentException("Subject with name '" + request.getName() + "' already exists.");
        });
        // 1️⃣ Fetch the course by ID
        Courses course = courseRepository.findById(request.getCourse_id())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + request.getCourse_id()));

         //2️⃣ Create the subject and set course reference
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        subject.setInstructorName(request.getInstructorName());
        subject.setImage(request.getImage());
        subject.setTotalVisit(0);
        subject.setEnable(request.isEnable());
        subject.setCourse(course); // ✅ Set the actual Course object, not just ID


        // 3️⃣ Save subject
        try{
        return subjectRepository.save(subject);
        } catch (DataIntegrityViolationException e) {
            // fallback in case DB constraint triggers
            throw new IllegalArgumentException("Duplicate subject name not allowed: " + request.getName());
        }
    }





}
