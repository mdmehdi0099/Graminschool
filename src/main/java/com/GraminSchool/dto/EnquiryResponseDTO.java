package com.GraminSchool.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnquiryResponseDTO {
    private String subject;
    private String email;
    private String phone;
    private String message;
    private String createdBy;
    private String resolvedBy;
    private boolean resolved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
