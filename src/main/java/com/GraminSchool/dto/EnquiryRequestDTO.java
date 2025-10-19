package com.GraminSchool.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnquiryRequestDTO {
    private String subject;
    private String email;
    private String phone;
    private String message;
}
