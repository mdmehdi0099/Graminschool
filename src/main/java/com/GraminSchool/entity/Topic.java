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
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name",nullable = false,unique = true)
    private String name;
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

    // ✅ Many topics belong to one chapter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    // ✅ One topic has many lectures
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lecture> lectures = new ArrayList<>();


}
