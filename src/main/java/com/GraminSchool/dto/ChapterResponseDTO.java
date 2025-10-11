package com.GraminSchool.dto;

import com.GraminSchool.entity.Chapter;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChapterResponseDTO {
    private Long id;
    private String name;
    private String unit;
    private String image;
    private String videoLink;
    private boolean enable;
    private String other;
    private String createdAt;
    private String updatedAt;

    private SubjectSummary subject;

    @Data
    @Builder
    public static class SubjectSummary {
        private Long id;
        private String name;
        private String description;
        private CourseSummary course;
    }

    @Data
    @Builder
    public static class CourseSummary {
        private Long id;
        private String course_name;
    }

    public static ChapterResponseDTO fromEntity(Chapter chapter) {
        return ChapterResponseDTO.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .unit(chapter.getUnit())
                .image(chapter.getImage())
                .videoLink(chapter.getVideoLink())
                .enable(chapter.isEnable())
                .other(chapter.getOther())
                .createdAt(chapter.getCreatedAt() != null ? chapter.getCreatedAt().toString() : null)
                .updatedAt(chapter.getUpdatedAt() != null ? chapter.getUpdatedAt().toString() : null)
                .subject(chapter.getSubject() != null ? SubjectSummary.builder()
                        .id(chapter.getSubject().getId())
                        .name(chapter.getSubject().getName())
                        .description(chapter.getSubject().getDescription())
                        .course(chapter.getSubject().getCourse() != null ? CourseSummary.builder()
                                .id(chapter.getSubject().getCourse().getId())
                                .course_name(chapter.getSubject().getCourse().getCourseName())
                                .build() : null)
                        .build() : null)
                .build();
    }
}
