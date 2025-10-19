package com.GraminSchool.repo;

import com.GraminSchool.entity.Enquiry;
import com.GraminSchool.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry,Long> {
    List<Enquiry> findByResolved(Boolean enabled);
    List<Enquiry> findByCreatedByIgnoreCase(String createdBy);
    List<Enquiry> findByCreatedByRoleIgnoreCase(String createdBy);
    List<Enquiry> findByCreatedByRoleIgnoreCaseAndResolved(String role, boolean resolved);
    List<Enquiry> findByCreatedByIgnoreCaseAndResolved(String createdBy,boolean resolved);
}
