package com.GraminSchool.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {
    private String course_name;
    private String short_details;
    private String course_description;
    private double price;
    private String InstructorName;
    private String Course_Image;
    private int validity;
    private boolean enable;
    private String Other;
}
