package com.GraminSchool.dto;

import com.GraminSchool.entity.Subject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String InstructorName;
    private String image;
    private boolean enable;
    private String Other;
    private String createdAt;
    private String updatedAt;

    private CourseSummary course;

    @Data
    @Builder
    public static class CourseSummary {
        private Long id;
        private String course_name;
        private String course_description;
    }

    public static SubjectResponseDTO fromEntity(Subject subject) {
        return SubjectResponseDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .description(subject.getDescription())
                .image(subject.getImage())
                .enable(subject.isEnable())
                .Other(subject.getOther())
                .createdAt(subject.getCreatedAt() != null ? subject.getCreatedAt().toString() : null)
                .updatedAt(subject.getUpdatedAt() != null ? subject.getUpdatedAt().toString() : null)
                .course(subject.getCourse() != null ? CourseSummary.builder()
                        .id(subject.getCourse().getId())
                        .course_name(subject.getCourse().getCourse_name())
                        .course_description(subject.getCourse().getCourse_description())
                        .build() : null)
                .build();
    }
}