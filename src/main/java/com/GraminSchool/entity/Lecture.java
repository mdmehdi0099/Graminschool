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
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="image",nullable = true)
    private String image;
    @Column(name="VideoLink",nullable = false)
    private String VideoLink;
    @Column(name="TotalVisit")
    private double TotalVisit;
    @Column(name="enable",nullable = false)
    private boolean enable;
    @Column(name="MaxWatch",nullable = true)
    private int MaxWatch;
    @Column(name="Other",nullable = true)
    private String Other;
    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ✅ Many lectures belong to one topic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

}
