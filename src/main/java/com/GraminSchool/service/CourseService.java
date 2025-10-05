package com.GraminSchool.service;

import com.GraminSchool.entity.Courses;
import com.GraminSchool.models.CourseResponse;
import com.GraminSchool.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Courses createCourses(CourseResponse request){
        Courses courses=Courses.builder()
                .course_name(request.getCourse_name())
                .short_details(request.getShort_details())
                .course_description(request.getCourse_description())
                .Course_Image(request.getCourse_Image())
                .InstructorName(request.getInstructorName())
                .price(request.getPrice())
                .enable(request.isEnable())
                .Other(request.getOther())
                .validity(request.getValidity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .TotalEnrolled(0)
                .TotalVisit(0)
                .build();
        return courseRepository.save(courses);
    }




}
