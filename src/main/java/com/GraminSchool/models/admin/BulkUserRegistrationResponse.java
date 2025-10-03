package com.GraminSchool.models.admin;

import com.GraminSchool.entity.Role;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkUserRegistrationResponse {
    private String name;
    private String email;
    private String phone;
    private Role role;
    private String address;
    private boolean status;

}
