package com.GraminSchool.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureResponseDTO {
    private Long id;
    private String name;
    private String image;
    private String videoLink;
    private boolean enable;
    private int maxWatch;
    private String other;

    private TopicSummary topic;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopicSummary {
        private Long id;
        private String name;
        private ChapterSummary chapter;
    }

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
        private String course_name;
    }
}
