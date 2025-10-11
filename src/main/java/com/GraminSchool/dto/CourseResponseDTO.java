package com.GraminSchool.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDTO {
    private Long id;
    private String courseName;
    private String shortDetails;
    private String courseDescription;
    private double price;
    private String instructorName;
    private String courseImage;
    private int validity;
    private double totalVisit;
    private int totalEnrolled;
    private boolean enable;
    private String other;
}
