package com.GraminSchool.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequestDTO {
    private String email;
    private Integer otp;
    private String password;
    private String confirmPassword;
}
