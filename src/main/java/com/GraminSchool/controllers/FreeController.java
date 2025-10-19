package com.GraminSchool.controllers;

import com.GraminSchool.dto.CourseResponseDTO;
import com.GraminSchool.dto.EnquiryRequestDTO;
import com.GraminSchool.dto.NoticeDTO;
import com.GraminSchool.entity.Enquiry;
import com.GraminSchool.entity.Notice;
import com.GraminSchool.entity.User;
import com.GraminSchool.service.CourseService;
import com.GraminSchool.service.EnquiryService;
import com.GraminSchool.service.NoticeService;
import com.GraminSchool.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/free")
public class FreeController {
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EnquiryService enquiryService;
    @GetMapping("/user")
    public ResponseEntity<List<User>> user(){
        System.out.println("Getting users.....");
        List<User> userList=userService.getUsers();
        return ResponseEntity.ok(userList);
    }
    //Get the list of courses
    @GetMapping("/Course/List")
    public ResponseEntity<?> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
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
    public Enquiry createEnquiry(@RequestBody EnquiryRequestDTO enquiryRequest) {

        return enquiryService.createEnquiryByGuest(enquiryRequest.getSubject(),enquiryRequest.getEmail(),enquiryRequest.getPhone(),enquiryRequest.getMessage());
    }



}
