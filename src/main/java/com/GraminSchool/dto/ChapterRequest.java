package com.GraminSchool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterRequest {
    private String name;
    private String unit;
    private String image;
    private String videoLink;
    private boolean enable;
    private String other;
    private Long subject_id;

}