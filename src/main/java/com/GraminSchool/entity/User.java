package com.GraminSchool.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name",nullable = false)
    private String name;

    @Column(name="Email",unique = true,nullable = false)
    private String email;

    @Column(name="Phone",unique = true,nullable = false)
    private String phone;

    @Column(name="Password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="Address",nullable = false)
    private String address;


    private boolean status;

    @CreatedDate
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
