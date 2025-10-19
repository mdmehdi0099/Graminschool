package com.GraminSchool.service;


import com.GraminSchool.dto.EnquiryUpdateDTO;
import com.GraminSchool.entity.Enquiry;
import com.GraminSchool.entity.Notice;
import com.GraminSchool.repo.EnquiryRepository;
import com.GraminSchool.repo.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;

    public Enquiry createEnquiryByGuest(String subject,String email,String phone,String message){
        Enquiry enquiry=Enquiry.builder()
                .subject(subject)
                .email(email)
                .phone(phone)
                .message(message)
                .createdBy("Guest")
                .createdByRole("Guest")
                .resolved(false)
                .build();
        return enquiryRepository.save(enquiry);
    }
    public Enquiry createEnquiryByUser(String subject,String email,String phone,String message,String username){
        Enquiry enquiry=Enquiry.builder()
                .subject(subject)
                .email(email)
                .phone(phone)
                .message(message)
                .createdBy(username)
                .createdByRole("User")
                .resolved(false)
                .build();
        return enquiryRepository.save(enquiry);
    }
    public Enquiry createEnquiryByAgent(String subject,String email,String phone,String message,String username){
        Enquiry enquiry=Enquiry.builder()
                .subject(subject)
                .email(email)
                .phone(phone)
                .message(message)
                .createdBy(username)
                .createdByRole("Agent")
                .resolved(false)
                .build();
        return enquiryRepository.save(enquiry);
    }

    public List<Enquiry> getAllEnquiry(){
        return enquiryRepository.findAll();
    }
    public List<Enquiry> getUnReasolvedEnquiry(){
        return enquiryRepository.findByResolved(false);
    }
    public List<Enquiry> getReasolvedEnquiry(){
        return enquiryRepository.findByResolved(true);
    }

    public List<Enquiry> getEnquiryByRole(String role){
        return enquiryRepository.findByCreatedByRoleIgnoreCase(role);
    }
    public List<Enquiry> getEnquiryByRoleAndResolution(String role,boolean resolve){
        return enquiryRepository.findByCreatedByRoleIgnoreCaseAndResolved(role,resolve);
    }
    public List<Enquiry> getEnquiryByUsername(String username){
        return enquiryRepository.findByCreatedByIgnoreCase(username);
    }
    //findByCreatedByIgnoreCaseAndResolved(String createdBy,boolean resolved);
    public List<Enquiry> getEnquiryByUsernameAndResolution(String username,boolean resolved){
        return enquiryRepository.findByCreatedByIgnoreCaseAndResolved(username,resolved);
    }

    // ✅ Update enquiry based on DTO
    public Enquiry updateEnquiry(EnquiryUpdateDTO dto,String username,String role) {
        Enquiry enquiry = enquiryRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Enquiry not found with ID: " + dto.getId()));

        enquiry.setResolved(dto.isResolve());
        if (!username.equalsIgnoreCase("Admin")) {
            enquiry.setResolvedBy(username);
        }else{
            enquiry.setResolvedBy(dto.getResolvedBy());
        }
        // Optional: You can add a column for closingComment if it exists in entity
        enquiry.setClosingComment(dto.getClosingComment());
        enquiry.setUpdatedAt(LocalDateTime.now());
        enquiry.setUpdatedByRole(role);

        return enquiryRepository.save(enquiry);
    }



}
