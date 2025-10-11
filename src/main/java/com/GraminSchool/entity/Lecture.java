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
    @Column(name="Name",nullable = false,unique = true)
    private String name;
    @Column(name="Image",nullable = true)
    private String image;
    @Column(name="VideoLink",nullable = false)
    private String videoLink;
    @Column(name="TotalVisit")
    private double totalVisit;
    @Column(name="enable",nullable = false)
    private boolean enable;
    @Column(name="MaxWatch",nullable = true)
    private int maxWatch;
    @Column(name="Other",nullable = true)
    private String other;
    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ✅ Many lectures belong to one topic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

}
