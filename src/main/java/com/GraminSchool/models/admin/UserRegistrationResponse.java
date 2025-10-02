package com.GraminSchool.models.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationResponse {
    private String email;
    private String role;
}
