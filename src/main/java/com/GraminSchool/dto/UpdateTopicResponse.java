package com.GraminSchool.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTopicResponse {
    private Long id;
    private String name;
    private String image;
    private String videoLink;
    private boolean enable;
    private String other;
}
