package com.GraminSchool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicRequest {
    private String name;
    private String image;
    private String videoLink;
    private boolean enable;
    private String other;
    private Long chapter_id; // Parent Chapter ID
}
