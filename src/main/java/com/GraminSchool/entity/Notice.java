package com.GraminSchool.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String message;
    private String createdBy;
    private Boolean enabled = true;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
