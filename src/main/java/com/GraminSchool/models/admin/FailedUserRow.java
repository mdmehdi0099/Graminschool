package com.GraminSchool.models.admin;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FailedUserRow {
    private int rowNumber;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String address;
    private String status;
    private String errorMessage;
}
