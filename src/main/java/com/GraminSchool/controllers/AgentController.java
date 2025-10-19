package com.GraminSchool.controllers;

import com.GraminSchool.dto.CourseResponseDTO;
import com.GraminSchool.dto.EnquiryRequestDTO;
import com.GraminSchool.dto.EnquiryUpdateDTO;
import com.GraminSchool.dto.NoticeDTO;
import com.GraminSchool.entity.Enquiry;
import com.GraminSchool.entity.Notice;
import com.GraminSchool.entity.Role;
import com.GraminSchool.entity.User;
import com.GraminSchool.models.admin.BulkUserRegistrationResponse;
import com.GraminSchool.models.admin.FailedUserRow;
import com.GraminSchool.security.JwtHelper;
import com.GraminSchool.service.CourseService;
import com.GraminSchool.service.EnquiryService;
import com.GraminSchool.service.NoticeService;
import com.GraminSchool.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private EnquiryService enquiryService;

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

    // Create notice
    @PostMapping("/create-notice")
    public Notice createNotice(@RequestBody NoticeDTO requestNotice, HttpServletRequest request) {
        //Extract token from Authorization Header
        String header=request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token=header.substring(7);
        String username=jwtHelper.getUsernameFromToken(token);
        return noticeService.createNotice(requestNotice.getTitle(),requestNotice.getMessage(),username,"AGENT");
    }

    // list all notices
    @GetMapping("/notices")
    public List<Notice> getAllNotices(){
        return noticeService.getAllNotices();
    }

    //Active Notices
    @GetMapping("/active-notices")
    public List<Notice> getActiveNotices(){
        return noticeService.getActiveNotices();
    }

    //Notice by id
    @GetMapping("/notice/{id}")
    public Notice getNotice(@PathVariable long id){
        return noticeService.getNoticeById(id);
    }
    //get list of notices created by current agent user
    // ✅ Get all notices by role
    @GetMapping("/notice/role/{role}")
    public List<Notice> getNoticesByRole(@PathVariable String role) {
        return noticeService.getNoticesByRole(role);
    }

    @GetMapping("/notice/my-notices")
    public List<Notice> getMyNotices(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix
        String jwt = token.substring(7);

        // Extract username from token
        String username = jwtHelper.getUsernameFromToken(jwt);

        // Fetch notices created by that user
        return noticeService.getNoticesByCreatedBy(username);
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
        return enquiryService.createEnquiryByAgent(enquiryRequest.getSubject(),enquiryRequest.getEmail(),enquiryRequest.getPhone(),enquiryRequest.getMessage(),username);
    }

    @GetMapping("/enquiry/current-user")
    public List<Enquiry> currentUserEnquiry(@RequestParam(required = false) Boolean resolved,HttpServletRequest request) {
        //Extract token from Authorization Header
        String header=request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token=header.substring(7);
        String username=jwtHelper.getUsernameFromToken(token);
        return enquiryService.getEnquiryByUsername(username);
    }
    //getEnquiryByUsernameAndResolution
    @GetMapping("/enquiry/current-user-pending-enquiry")
    public List<Enquiry> currentUserPendingEnquiry(@RequestParam(required = false) Boolean resolved,HttpServletRequest request) {
        //Extract token from Authorization Header
        String header=request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token=header.substring(7);
        String username=jwtHelper.getUsernameFromToken(token);
        return enquiryService.getEnquiryByUsernameAndResolution(username,false);
    }
    @GetMapping("/enquiry/current-user-resolved-enquiry")
    public List<Enquiry> currentUserResolvedEnquiry(@RequestParam(required = false) Boolean resolved,HttpServletRequest request) {
        //Extract token from Authorization Header
        String header=request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token=header.substring(7);
        String username=jwtHelper.getUsernameFromToken(token);
        return enquiryService.getEnquiryByUsernameAndResolution(username,true);
    }
    //Update enquiry (mark resolved/unresolved + add comments)
    @PutMapping("/enquiry/update")
    public ResponseEntity<Enquiry> updateEnquiry(@RequestBody EnquiryUpdateDTO enquiryUpdateDTO,HttpServletRequest request) {
        //Extract token from Authorization Header
        String header=request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token=header.substring(7);
        String username=jwtHelper.getUsernameFromToken(token);
        Enquiry updatedEnquiry = enquiryService.updateEnquiry(enquiryUpdateDTO,username,"Agent");
        return ResponseEntity.ok(updatedEnquiry);
    }


}
