package com.GraminSchool.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="unit",nullable = false)
    private String unit;
    @Column(name="Image",nullable = true)
    private String image;
    @Column(name="VideoLink",nullable = true)
    private String VideoLink;
    @Column(name="TotalVisit")
    private double TotalVisit;
    @Column(name="enable",nullable = false)
    private boolean enable;
    @Column(name="Other",nullable = true)
    private String Other;
    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;




}
