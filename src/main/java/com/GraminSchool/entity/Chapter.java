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
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name",nullable = false,unique = true)
    private String name;
    @Column(name="unit",nullable = false)
    private String unit;
    @Column(name="Image",nullable = true)
    private String image;
    @Column(name="VideoLink",nullable = true)
    private String VideoLink;
    @Column(name="TotalVisit",nullable = true)
    private double TotalVisit;
    @Column(name="enable",nullable = false)
    private boolean enable;
    @Column(name="Other",nullable = true)
    private String Other;
    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ✅ Many chapters belong to one subject
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // ✅ One chapter has many topics
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Topic> topics = new ArrayList<>();


}
