package com.GraminSchool.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubjectResponse {
    private Long id;
    private String name;
    private String description;
    private String instructorName;
    private String image;
    private boolean enable;
    private String other;
}
