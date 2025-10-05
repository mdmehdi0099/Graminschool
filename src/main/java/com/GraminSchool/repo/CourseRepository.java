package com.GraminSchool.repo;

import com.GraminSchool.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Courses, Long> {
    // Fetch course with all nested relations in one go
    @Query("SELECT c FROM Courses c " +
            "LEFT JOIN FETCH c.subjects s " +
            "LEFT JOIN FETCH s.chapters ch " +
            "LEFT JOIN FETCH ch.topics t " +
            "LEFT JOIN FETCH t.lectures l " +
            "WHERE c.id = :courseId")
    Courses findCourseWithFullHierarchy(@Param("courseId") Long courseId);

}
