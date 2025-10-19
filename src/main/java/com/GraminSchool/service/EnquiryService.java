package com.GraminSchool.service;


import com.GraminSchool.entity.Enquiry;
import com.GraminSchool.entity.Notice;
import com.GraminSchool.repo.EnquiryRepository;
import com.GraminSchool.repo.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .resolved(false)
                .build();
        return enquiryRepository.save(enquiry);
    }

}
