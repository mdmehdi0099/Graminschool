package com.GraminSchool.controllers;

import com.GraminSchool.dto.CourseResponseDTO;
import com.GraminSchool.entity.Notice;
import com.GraminSchool.entity.Role;
import com.GraminSchool.entity.User;
import com.GraminSchool.models.admin.BulkUserRegistrationResponse;
import com.GraminSchool.models.admin.FailedUserRow;
import com.GraminSchool.service.CourseService;
import com.GraminSchool.service.NoticeService;
import com.GraminSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agent")
public class AgentController {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/user")
    public ResponseEntity<List<User>> user(){
        System.out.println("Getting users.....");
        List<User> userList=userService.getUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/normal-users")
    public ResponseEntity<List<User>> getNormalUser(){
        System.out.println("Getting normal users.....");
        List<User> userList=userService.getNormalUser();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/create-bulk-Normal-user")
    public ResponseEntity<List<BulkUserRegistrationResponse>> registerBulkNormalUser(@RequestParam("file") MultipartFile file) {
        try {
            // Call service method (it now returns Map with both saved & failed rows)
            Map<String, Object> result = userService.saveUsersFromFileForNormalUser(file);

            @SuppressWarnings("unchecked")
            List<User> savedUsers = (List<User>) result.get("savedUsers");

            @SuppressWarnings("unchecked")
            List<FailedUserRow> failedUsers = (List<FailedUserRow>) result.get("failedRows");

            // ✅ If no failures, return 200 OK
            if (failedUsers == null || failedUsers.isEmpty()) {
                List<BulkUserRegistrationResponse> responseList = savedUsers.stream()
                        .map(user -> new BulkUserRegistrationResponse(
                                user.getName(),
                                user.getEmail(),
                                user.getPhone(),
                                user.getRole(),
                                user.getAddress(),
                                user.isStatus(),
                                null // no error
                        ))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(responseList);
            }

            // ❌ If any failures, return 400 with failed records
            List<BulkUserRegistrationResponse> failedResponseList = failedUsers.stream()
                    .map(row -> new BulkUserRegistrationResponse(
                            row.getName(),
                            row.getEmail(),
                            row.getPhone(),
                            row.getRole() != null ? Role.valueOf(row.getRole()) : null,
                            row.getAddress(),
                            Boolean.parseBoolean(row.getStatus()),
                            row.getErrorMessage()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(failedResponseList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
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
