package com.GraminSchool.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureRequest {
    private String name;
    private String image;
    private String videoLink;
    private boolean enable;
    private int maxWatch;
    private String other;
    private Long topic_id; // foreign key to Topic
}