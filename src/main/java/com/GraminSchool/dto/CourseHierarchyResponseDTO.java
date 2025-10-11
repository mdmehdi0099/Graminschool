package com.GraminSchool.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseHierarchyResponseDTO {
    private Long id;
    private String course_name;
    private boolean enable;
    private List<SubjectResponse> subjects;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SubjectResponse {
        private Long id;
        private String name;
        private String description;
        private String instructorName;
        private String image;
        private boolean enable;
        private List<ChapterResponse> chapters;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChapterResponse {
        private Long id;
        private String name;
        private String unit;
        private String image;
        private boolean enable;
        private List<TopicResponse> topics;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopicResponse {
        private Long id;
        private String name;
        private String image;
        private String videoLink;
        private boolean enable;
        private List<LectureResponse> lectures;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LectureResponse {
        private Long id;
        private String name;
        private String image;
        private String videoLink;
        private boolean enable;
        private int maxWatch;
        private String other;
    }

}
