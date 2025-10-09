package com.GraminSchool.controllers;

import com.GraminSchool.dto.*;
import com.GraminSchool.entity.*;
import com.GraminSchool.models.admin.BulkUserRegistrationResponse;
import com.GraminSchool.models.admin.FailedUserRow;
import com.GraminSchool.models.admin.UserRegistrationResponse;
import com.GraminSchool.repo.CourseRepository;
import com.GraminSchool.service.*;
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
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private LectureService lectureService;

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

    @PostMapping("/Create-Course")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest request) {
        try {
            // Basic field validation (optional)
            if (request.getCourse_name() == null || request.getCourse_name().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Course name is required.");
            }
            if (request.getShort_details() == null || request.getShort_details().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Short details are required.");
            }
            if (request.getCourse_description() == null || request.getCourse_description().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Course description is required.");
            }
            if (request.getPrice() <= 0) {
                return ResponseEntity.badRequest().body("Price must be greater than zero.");
            }

            // Save course
            Courses createdCourse = courseService.createCourses(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating course: " + e.getMessage());
        }
    }

    @PostMapping("/Create-Subject")
    public ResponseEntity<?> createSubject(@RequestBody SubjectRequest subjectRequest) {
        try {
            // Basic field validation (optional)
            if (subjectRequest.getName() == null || subjectRequest.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("subject name is required.");
            }
            if (subjectRequest.getDescription() == null || subjectRequest.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Description is required.");
            }


            // Save course
            // ✅ Save subject through service
            Subject subject = subjectService.saveSubject(subjectRequest);
            //Subject subject =  subjectService.saveSubject(Subjectrequest);
            SubjectResponseDTO subjectResponseDTO=SubjectResponseDTO.fromEntity(subject);
            return ResponseEntity.status(HttpStatus.CREATED).body(subjectResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating course: " + e.getMessage());
        }
    }

    @PostMapping("/Create-Chapter")
    public ResponseEntity<?> createChapter(@RequestBody ChapterRequest request){
        try{
            if(request.getName()==null||request.getName().trim().isEmpty()){
                return ResponseEntity.badRequest().body("Chapter name is required.");
            }
            if(request.getUnit()==null||request.getUnit().trim().isEmpty()){
                return ResponseEntity.badRequest().body("Chapter unit is required.");
            }
            Chapter savedChapter=chapterService.saveChapter(request);

            ChapterResponseDTO.ChapterResponseDTOBuilder responseDTO=ChapterResponseDTO.builder()
                    .id(savedChapter.getId())
                    .name(savedChapter.getName())
                    .unit(savedChapter.getUnit())
                    .image(savedChapter.getImage())
                    .enable(savedChapter.isEnable())
                    .subject(ChapterResponseDTO.SubjectSummary.builder()
                        .id(savedChapter.getSubject().getId())
                        .name(savedChapter.getSubject().getName())
                        .description(savedChapter.getSubject().getDescription())
                        .course(ChapterResponseDTO.CourseSummary.builder()
                                .id(savedChapter.getSubject().getCourse().getId())
                                .course_name(savedChapter.getSubject().getCourse().getCourse_name())
                        .build())
                    .build());


            return ResponseEntity.status(HttpStatus.CREATED).body(ChapterResponseDTO.fromEntity(savedChapter));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Input : "+e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating chapter: "+e.getMessage());
        }
    }

    @PostMapping("/Create-Topic")
    public ResponseEntity<?> createTopic(@RequestBody TopicRequest request) {
        try {
            // ✅ Basic validation
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Topic name is required.");
            }

            if (request.getChapter_id() == null) {
                return ResponseEntity.badRequest().body("Chapter ID is required.");
            }

            TopicResponseDTO response = topicService.saveTopic(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating topic: " + e.getMessage());
        }
    }

    @PostMapping("/Create-Lecture")
    public ResponseEntity<?> createLecture(@RequestBody LectureRequest request) {
        try {
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Lecture name is required.");
            }
            if (request.getTopic_id() == null) {
                return ResponseEntity.badRequest().body("Topic ID is required.");
            }

            LectureResponseDTO response = lectureService.saveLecture(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating lecture: " + e.getMessage());
        }
    }







}
