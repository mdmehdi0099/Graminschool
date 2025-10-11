package com.GraminSchool.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="course_name",nullable = false,unique = true)
    private String courseName;
    @Column(name="short_details",nullable = false)
    private String shortDetails;
    @Column(name="course_description",nullable = false)
    private String courseDescription;
    @Column(name="price",nullable = false)
    private double price;
    @Column(name="InstructorName",nullable = true)
    private String instructorName;
    @Column(name="Course_Image",nullable = true)
    private String courseImage;
    @Column(name="validity",nullable = true)
    private int validity;
    @Column(name="TotalVisit")
    private double totalVisit;
    @Column(name="TotalEnrolled")
    private int totalEnrolled;
    @Column(name="enable",nullable = false)
    private boolean enable;
    @Column(name="Other",nullable = true)
    private String other;
    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ✅ Relationship
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subject> subjects = new ArrayList<>();


}
