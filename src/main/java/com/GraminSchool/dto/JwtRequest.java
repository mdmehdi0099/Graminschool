package com.GraminSchool.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtRequest {


    private String email;

    private String password;

    private String role;
}
