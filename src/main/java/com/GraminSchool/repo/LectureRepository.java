package com.GraminSchool.repo;

import com.GraminSchool.entity.Lecture;
import org.hibernate.sql.exec.spi.JdbcParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture,Long> {
    Optional<Lecture> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
