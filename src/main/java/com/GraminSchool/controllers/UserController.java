package com.GraminSchool.controllers;


import com.GraminSchool.dto.EnquiryRequestDTO;
import com.GraminSchool.entity.Enquiry;
import com.GraminSchool.entity.Notice;
import com.GraminSchool.entity.User;
import com.GraminSchool.security.JwtHelper;
import com.GraminSchool.service.EnquiryService;
import com.GraminSchool.service.NoticeService;
import com.GraminSchool.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private EnquiryService enquiryService;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/user")
    public ResponseEntity<List<User>> user(){
        System.out.println("Getting users.....");
        List<User> userList=userService.getUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/active-notices")
    public List<Notice> getActiveNotices(){
        return noticeService.getActiveNotices();
    }

    @GetMapping("/notice/{id}")
    public Notice getNotice(@PathVariable long id){
        return noticeService.getNoticeById(id);
    }

    // Create enquiry
    @PostMapping("/create-enquiry")
    public Enquiry createEnquiry(@RequestBody EnquiryRequestDTO enquiryRequest, HttpServletRequest request) {
        //Extract token from Header
        String header=request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token=header.substring(7);
        String username=jwtHelper.getUsernameFromToken(token);
        return enquiryService.createEnquiryByUser(enquiryRequest.getSubject(),enquiryRequest.getEmail(),enquiryRequest.getPhone(),enquiryRequest.getMessage(),username);
    }

}
