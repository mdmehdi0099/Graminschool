package com.GraminSchool.service;

import com.GraminSchool.dto.CourseResponseDTO;
import com.GraminSchool.entity.Courses;
import com.GraminSchool.dto.CourseRequest;
import com.GraminSchool.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Courses createCourses(CourseRequest request){
        Courses courses=Courses.builder()
                .courseName(request.getCourse_name())
                .shortDetails(request.getShort_details())
                .courseDescription(request.getCourse_description())
                .courseImage(request.getCourse_Image())
                .instructorName(request.getInstructorName())
                .price(request.getPrice())
                .enable(request.isEnable())
                .other(request.getOther())
                .validity(request.getValidity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalEnrolled(0)
                .totalVisit(0)
                .build();
        return courseRepository.save(courses);
    }

    /////////////////jkhfghfhfhgf
    public List<CourseResponseDTO> getAllCourses() {
        List<Courses> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> CourseResponseDTO.builder()
                        .id(course.getId())
                        .courseName(course.getCourseName())
                        .shortDetails(course.getShortDetails())
                        .courseDescription(course.getCourseDescription())
                        .price(course.getPrice())
                        .instructorName(course.getInstructorName())
                        .courseImage(course.getCourseImage())
                        .validity(course.getValidity())
                        .totalVisit(course.getTotalVisit())
                        .totalEnrolled(course.getTotalEnrolled())
                        .enable(course.isEnable())
                        .other(course.getOther())
                        .build())
                .collect(Collectors.toList());
    }




}
