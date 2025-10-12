package com.GraminSchool.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private String title;
    private String message;
    private String createdBy;
    private Boolean enabled; // optional, for status toggle if needed
}
