package com.GraminSchool.controllers;


import com.GraminSchool.entity.Courses;
import com.GraminSchool.repo.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;

//    @GetMapping("/{courseId}")
//    public ResponseEntity<Courses> getCourseDetails(@PathVariable Long courseId) {
//        Courses course = courseRepository.findCourseWithFullHierarchy(courseId);
//        if (course == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(course);
//    }



}
