package com.GraminSchool.dto;


import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordResponseDTO {
    private String email;
    private String message;
}
