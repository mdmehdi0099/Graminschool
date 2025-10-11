package com.GraminSchool.repo;

import com.GraminSchool.entity.Chapter;
import com.GraminSchool.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    Optional<Chapter> findByNameIgnoreCase(String name);

    // ✅ Check if a subject with the given name already exists
    boolean existsByNameIgnoreCase(String name);
}

