package com.GraminSchool.service;

import com.GraminSchool.dto.CourseHierarchyResponseDTO;
import com.GraminSchool.entity.Courses;
import com.GraminSchool.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CourseHierarchyService {
    @Autowired
    private CourseRepository courseRepository;

    public CourseHierarchyResponseDTO getCourseHierarchy(Long courseId) {
        Courses course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        return CourseHierarchyResponseDTO.builder()
                .id(course.getId())
                .course_name(course.getCourseName())
                .enable(course.isEnable())
                .subjects(
                        course.getSubjects().stream().map(subject ->
                                CourseHierarchyResponseDTO.SubjectResponse.builder()
                                        .id(subject.getId())
                                        .name(subject.getName())
                                        .description(subject.getDescription())
                                        .instructorName(subject.getInstructorName())
                                        .image(subject.getImage())
                                        .enable(subject.isEnable())
                                        .chapters(
                                                subject.getChapters().stream().map(chapter ->
                                                        CourseHierarchyResponseDTO.ChapterResponse.builder()
                                                                .id(chapter.getId())
                                                                .name(chapter.getName())
                                                                .unit(chapter.getUnit())
                                                                .image(chapter.getImage())
                                                                .enable(chapter.isEnable())
                                                                .topics(
                                                                        chapter.getTopics().stream().map(topic ->
                                                                                CourseHierarchyResponseDTO.TopicResponse.builder()
                                                                                        .id(topic.getId())
                                                                                        .name(topic.getName())
                                                                                        .image(topic.getImage())
                                                                                        .videoLink(topic.getVideoLink())
                                                                                        .enable(topic.isEnable())
                                                                                        .lectures(
                                                                                                topic.getLectures().stream().map(lecture ->
                                                                                                        CourseHierarchyResponseDTO.LectureResponse.builder()
                                                                                                                .id(lecture.getId())
                                                                                                                .name(lecture.getName())
                                                                                                                .image(lecture.getImage())
                                                                                                                .videoLink(lecture.getVideoLink())
                                                                                                                .enable(lecture.isEnable())
                                                                                                                .maxWatch(lecture.getMaxWatch())
                                                                                                                .other(lecture.getOther())
                                                                                                                .build()
                                                                                                ).collect(Collectors.toList())
                                                                                        ).build()
                                                                        ).collect(Collectors.toList())
                                                                ).build()
                                                ).collect(Collectors.toList())
                                        ).build()
                        ).collect(Collectors.toList())
                ).build();

    }
}
