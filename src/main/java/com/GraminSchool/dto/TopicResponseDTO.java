package com.GraminSchool.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicResponseDTO {
    private Long id;
    private String name;
    private String image;
    private String videoLink;
    private boolean enable;
    private String other;

    private ChapterSummary chapter;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChapterSummary {
        private Long id;
        private String name;
        private String unit;
        private SubjectSummary subject;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SubjectSummary {
        private Long id;
        private String name;
        private String description;
        private CourseSummary course;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseSummary {
        private Long id;
        private String courseName;
    }
}
