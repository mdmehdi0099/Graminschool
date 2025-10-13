package com.GraminSchool.controllers;

import com.GraminSchool.dto.CourseResponseDTO;
import com.GraminSchool.entity.Notice;
import com.GraminSchool.entity.User;
import com.GraminSchool.service.CourseService;
import com.GraminSchool.service.NoticeService;
import com.GraminSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
