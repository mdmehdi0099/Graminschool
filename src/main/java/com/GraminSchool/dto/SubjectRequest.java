package com.GraminSchool.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SubjectRequest {
    private String name;
    private String description;
    private String InstructorName;
    private String image;
    private boolean enable;
    private String Other;
    private Long course_id;


}
