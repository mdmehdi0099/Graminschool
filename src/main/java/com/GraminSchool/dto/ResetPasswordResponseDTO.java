package com.GraminSchool.dto;


import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordResponseDTO {
    private Email email;
    private String Message;
}
