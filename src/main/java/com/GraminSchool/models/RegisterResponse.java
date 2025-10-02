package com.GraminSchool.models;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String token;
    private String email;
    private String role;
}
