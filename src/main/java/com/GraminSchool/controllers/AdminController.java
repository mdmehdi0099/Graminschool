package com.GraminSchool.controllers;

import com.GraminSchool.entity.Courses;
import com.GraminSchool.entity.Role;
import com.GraminSchool.entity.User;
import com.GraminSchool.models.RegisterResponse;
import com.GraminSchool.models.admin.BulkUserRegistrationResponse;
import com.GraminSchool.models.admin.FailedUserRow;
import com.GraminSchool.models.admin.UserRegistrationResponse;
import com.GraminSchool.repo.CourseRepository;
import com.GraminSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseRepository courseRepository;

    //Getting the list of users
    @GetMapping("/user")
    public ResponseEntity<List<User>> user(){
        System.out.println("Getting users.....");
        List<User> userList=userService.getUsers();
        return ResponseEntity.ok(userList);
    }
    //get agent list
    @GetMapping("/agent-users")
    public ResponseEntity<List<User>> getAgentUser(){
        System.out.println("Getting agent users.....");
        List<User> userList=userService.getAgents();
        return ResponseEntity.ok(userList);
    }
    //get agent list
    @GetMapping("/normal-users")
    public ResponseEntity<List<User>> getNormalUser(){
        System.out.println("Getting normal users.....");
        List<User> userList=userService.getNormalUser();
        return ResponseEntity.ok(userList);
    }

    //Admin will be Creating the normal users
    @PostMapping("/create-normal-user")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody User request){
        UserRegistrationResponse response=userService.registerNormalUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Admin will be Creating the Agent users
    @PostMapping("/create-agent-user")
    public ResponseEntity<UserRegistrationResponse> registerAgentUser(@RequestBody User request){
        UserRegistrationResponse response=userService.registerAgentUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Admin will be Creating the Agent users
    @PostMapping("/create-bulk-Agent-user")
    public ResponseEntity<List<BulkUserRegistrationResponse>> registerBulkUser(@RequestParam("file") MultipartFile file) {
        try {
            // Call service method (it now returns Map with both saved & failed rows)
            Map<String, Object> result = userService.saveUsersFromFile(file);

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




    @GetMapping("/course/{courseId}")
    public ResponseEntity<Courses> getCourseDetails(@PathVariable Long courseId) {
        Courses course = courseRepository.findCourseWithFullHierarchy(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }









}
