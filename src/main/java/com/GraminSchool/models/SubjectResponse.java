package com.GraminSchool.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectResponse {
    private String name;
    private String description;
    private String InstructorName;
    private String image;
    private boolean enable;
    private String Other;
    private Long course_id;


}
