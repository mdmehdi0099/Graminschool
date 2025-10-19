package com.GraminSchool.controllers;

import com.GraminSchool.dto.*;
import com.GraminSchool.entity.*;
import com.GraminSchool.models.admin.BulkUserRegistrationResponse;
import com.GraminSchool.models.admin.FailedUserRow;
import com.GraminSchool.models.admin.UserRegistrationResponse;
import com.GraminSchool.repo.*;
import com.GraminSchool.security.JwtHelper;
import com.GraminSchool.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private CourseHierarchyService courseHierarchyService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private EnquiryService enquiryService;

    @Autowired
    private JwtHelper jwtHelper;

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

            //If no failures, return 200 OK
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

            //If any failures, return 400 with failed records
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

            // If no failures, return 200 OK
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

            //If any failures, return 400 with failed records
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

    /*
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Courses> getCourseDetails(@PathVariable Long courseId) {
        Courses course = courseRepository.findCourseWithFullHierarchy(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

 */

    //create course
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

    //Get the list of courses
    @GetMapping("/Course/List")
    public ResponseEntity<?> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    //create subject
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
            //Save subject through service
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

    //create chapter
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
                                .course_name(savedChapter.getSubject().getCourse().getCourseName())
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

    //create topic
    @PostMapping("/Create-Topic")
    public ResponseEntity<?> createTopic(@RequestBody TopicRequest request) {
        try {
            //Basic validation
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

    //create lecture
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

    //get course hierarchy details by course id
    @GetMapping("/Course/{courseId}/details")
    public ResponseEntity<?> getCourseHierarchyDetail(@PathVariable Long courseId) {
        try {
            CourseHierarchyResponseDTO courseHierarchyResponseDTO = courseHierarchyService.getCourseHierarchy(courseId);
            return ResponseEntity.ok(courseHierarchyResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching hierarchy: " + e.getMessage());
        }
    }

    //update course detail
    @PutMapping("/Course/Update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,@RequestBody Courses updatedCourses){
        Optional<Courses> existingCourseOpt=courseRepository.findById(id);
        if (existingCourseOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Course not found with ID: "+id);
        }
        Courses existingCourse=existingCourseOpt.get();
        //checking the uniqueness of the course name
        if (updatedCourses.getCourseName()!=null && !updatedCourses.getCourseName().equalsIgnoreCase(existingCourse.getCourseName())){
            boolean nameExists=courseRepository.existsByCourseNameIgnoreCase(updatedCourses.getCourseName());
            if (nameExists){
                return ResponseEntity.badRequest().body("Course name already exists!");
            }
            existingCourse.setCourseName(updatedCourses.getCourseName());
        }
        //update all other non null fields
        if (updatedCourses.getShortDetails()!=null){
            existingCourse.setShortDetails(updatedCourses.getShortDetails());
        }
        if (updatedCourses.getCourseDescription()!=null){
            existingCourse.setCourseDescription(updatedCourses.getCourseDescription());
        }
        if (updatedCourses.getPrice()!=0){
            existingCourse.setPrice(updatedCourses.getPrice());
        }
        if (updatedCourses.getInstructorName()!=null){
            existingCourse.setInstructorName(updatedCourses.getInstructorName());
        }
        if (updatedCourses.getCourseImage()!=null){
            existingCourse.setCourseImage(updatedCourses.getCourseImage());
        }
        if (updatedCourses.getValidity()!=0){
            existingCourse.setValidity(updatedCourses.getValidity());
        }
        existingCourse.setEnable(updatedCourses.isEnable());
        if (updatedCourses.getOther()!=null){
            existingCourse.setOther(updatedCourses.getOther());
        }
        //save the updated entity
        Courses courses=courseRepository.save(existingCourse);
        CourseResponseDTO courseResponseDTO=new CourseResponseDTO(
                courses.getId(),
                courses.getCourseName(),
                courses.getShortDetails(),
                courses.getCourseDescription(),
                courses.getPrice(),
                courses.getInstructorName(),
                courses.getCourseImage(),
                courses.getValidity(),
                courses.getTotalVisit(),
                courses.getTotalEnrolled(),
                courses.isEnable(),
                courses.getOther()
        );
        return ResponseEntity.ok(courseResponseDTO);
    }

    @PutMapping("/Subject/Update/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable Long id,@RequestBody Subject updatedSubject){
        Optional<Subject> existingSubjectOpt= subjectRepository.findById(id);

        if(existingSubjectOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Subject not found with ID: "+id);
        }
        Subject existingSubject=existingSubjectOpt.get();
        //checking the uniqueness of the subject name
        if (updatedSubject.getName()!=null && !updatedSubject.getName().equalsIgnoreCase(existingSubject.getName())){
            boolean nameExists=subjectRepository.existsByNameIgnoreCase(updatedSubject.getName());
            if (nameExists){
                return ResponseEntity.badRequest().body("Subject name already exists!");
            }
            existingSubject.setName(updatedSubject.getName());
        }
        //update other fields
        if (updatedSubject.getDescription()!=null){
            existingSubject.setDescription(updatedSubject.getDescription());
        }
        if (updatedSubject.getInstructorName()!=null){
            existingSubject.setInstructorName(updatedSubject.getInstructorName());
        }
        if (updatedSubject.getImage()!=null){
            existingSubject.setImage(updatedSubject.getImage());
        }

        existingSubject.setEnable(updatedSubject.isEnable());

        if (updatedSubject.getOther()!=null){
            existingSubject.setOther(updatedSubject.getOther());
        }

        //save the updated entity
        Subject subject=subjectRepository.save(existingSubject);


        //convert the reponse to DTO
        UpdateSubjectResponse updateSubjectResponse=new UpdateSubjectResponse(
                subject.getId(),
                subject.getName(),
                subject.getDescription(),
                subject.getInstructorName(),
                subject.getImage(),
                subject.isEnable(),
                subject.getOther()
        );

        return ResponseEntity.ok(updateSubjectResponse);

    }

    //update chapter
    @PutMapping("/Chapter/Update/{id}")
    public ResponseEntity<?> updateChapter(@PathVariable Long id,@RequestBody Chapter updatedChapter){
        Optional<Chapter> existingChapterOpt= chapterRepository.findById(id);
        if(existingChapterOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Chapter not found with ID: "+id);
        }
        Chapter existingChapter=existingChapterOpt.get();
        //checking the uniqueness of the chapter name
        if (updatedChapter.getName()!=null && !updatedChapter.getName().equalsIgnoreCase(existingChapter.getName())){
            boolean nameExists=chapterRepository.existsByNameIgnoreCase(updatedChapter.getName());
            if (nameExists){
                return ResponseEntity.badRequest().body("Chapter name already exists!");
            }
            existingChapter.setName(updatedChapter.getName());
        }
        //update other fields
        if (updatedChapter.getUnit()!=null){
            existingChapter.setUnit(updatedChapter.getUnit());
        }
        if (updatedChapter.getImage()!=null){
            existingChapter.setImage(updatedChapter.getImage());
        }
        if (updatedChapter.getVideoLink()!=null){
            existingChapter.setVideoLink(updatedChapter.getVideoLink());
        }
        existingChapter.setEnable(updatedChapter.isEnable());
        if (updatedChapter.getOther()!=null){
            existingChapter.setOther(updatedChapter.getOther());
        }
        //save the updated entity
        Chapter chapter=chapterRepository.save(existingChapter);

        //convert the reponse to DTO
        UpdateChapterResponse updateChapterResponse=new UpdateChapterResponse(
                chapter.getId(),
                chapter.getName(),
                chapter.getUnit(),
                chapter.getImage(),
                chapter.getVideoLink(),
                chapter.isEnable(),
                chapter.getOther()
        );
        return ResponseEntity.ok(updateChapterResponse);
    }

    //update Topic
    @PutMapping("/Topic/Update/{id}")
    public ResponseEntity<?> updateTopic(@PathVariable Long id,@RequestBody Topic updatedTopic){
        Optional<Topic> existingTopicOpt=topicRepository.findById(id);
        if(existingTopicOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Topic with ID: "+id);
        }
        Topic existingTopic=existingTopicOpt.get();
        //checking the uniqueness of the topic name
        if (updatedTopic.getName()!=null && !updatedTopic.getName().equalsIgnoreCase(existingTopic.getName())){
            boolean nameExists=topicRepository.existsByNameIgnoreCase(updatedTopic.getName());
            if (nameExists){
                return ResponseEntity.badRequest().body("Topic name already exists!");
            }
            existingTopic.setName(updatedTopic.getName());
        }
        //update other fields
        if (updatedTopic.getImage()!=null){
            existingTopic.setImage(updatedTopic.getImage());
        }
        if (updatedTopic.getVideoLink()!=null){
            existingTopic.setVideoLink(updatedTopic.getVideoLink());
        }
        existingTopic.setEnable(updatedTopic.isEnable());
        if (updatedTopic.getOther()!=null){
            existingTopic.setOther(updatedTopic.getOther());
        }
        //save the updated entity
        Topic topic=topicRepository.save(existingTopic);

        //convert the reponse to DTO
        UpdateTopicResponse updateTopicResponse=new UpdateTopicResponse(
                topic.getId(),
                topic.getName(),
                topic.getImage(),
                topic.getVideoLink(),
                topic.isEnable(),
                topic.getOther()
        );
        return ResponseEntity.ok(updateTopicResponse);
    }

    //Lecture
    @PutMapping("/Lecture/Update/{id}")
    public ResponseEntity<?> updateLecture(@PathVariable Long id,@RequestBody Lecture updatedLecture){
        Optional<Lecture> existingLectureOpt=lectureRepository.findById(id);
        if(existingLectureOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Lecture with ID: "+id);
        }
        Lecture existingLecture=existingLectureOpt.get();
        //checking the uniqueness of the lecture name
        if (updatedLecture.getName()!=null && !updatedLecture.getName().equalsIgnoreCase(existingLecture.getName())){
            boolean nameExists=topicRepository.existsByNameIgnoreCase(updatedLecture.getName());
            if (nameExists){
                return ResponseEntity.badRequest().body("Lecture name already exists!");
            }
            existingLecture.setName(updatedLecture.getName());
        }
        //update other fields
        if (updatedLecture.getImage()!=null){
            existingLecture.setImage(updatedLecture.getImage());
        }
        if (updatedLecture.getVideoLink()!=null){
            existingLecture.setVideoLink(updatedLecture.getVideoLink());
        }
        existingLecture.setEnable(updatedLecture.isEnable());
        if (updatedLecture.getMaxWatch()!=0){
            existingLecture.setMaxWatch(updatedLecture.getMaxWatch());
        }

        if (updatedLecture.getOther()!=null){
            existingLecture.setOther(updatedLecture.getOther());
        }
        //save the updated entity
        Lecture lecture=lectureRepository.save(existingLecture);

        //convert the reponse to DTO
        UpdateLectureResponse updateLectureResponse=new UpdateLectureResponse(
                lecture.getId(),
                lecture.getName(),
                lecture.getImage(),
                lecture.getVideoLink(),
                lecture.isEnable(),
                lecture.getMaxWatch(),
                lecture.getOther()
        );
        return ResponseEntity.ok(updateLectureResponse);
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
        return noticeService.createNotice(requestNotice.getTitle(),requestNotice.getMessage(),username,"ADMIN");
    }

    // ✅ Get all notices by role
    @GetMapping("/notice/role/{role}")
    public List<Notice> getNoticesByRole(@PathVariable String role) {
        return noticeService.getNoticesByRole(role);
    }
    //get the list of my notices only
    @GetMapping("/notice/my-notices")
    public List<Notice> getMyNotices(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix
        String jwt = token.substring(7);

        // Extract username from token
        String username = jwtHelper.getUsernameFromToken(jwt);

        // Fetch notices created by that user
        return noticeService.getNoticesByCreatedBy(username);
    }

    // update notice
    @PutMapping("/notice/{id}")
    public Notice updateNotice(@PathVariable Long id, @RequestBody NoticeDTO noticeDTO) {
        return noticeService.updateNotice(id, noticeDTO.getTitle(), noticeDTO.getMessage(),noticeDTO.getEnabled());
    }


    // list all notices
    @GetMapping("/notices")
    public List<Notice> getAllNotices(){
        return noticeService.getAllNotices();
    }
    // list all active notices
    @GetMapping("/active-notices")
    public List<Notice> getActiveNotices(){
        return noticeService.getActiveNotices();
    }
    // get notice by id
    @GetMapping("/notice/{id}")
    public Notice getNotice(@PathVariable long id){
        return noticeService.getNoticeById(id);
    }

    // list all enquiry
    @GetMapping("/enquiry")
    public List<Enquiry> getAllEnquiry(){
        return enquiryService.getAllEnquiry();
    }

    // list all unresolved enquiry
    @GetMapping("/enquiry/unreasolved")
    public List<Enquiry> getUnReasolvedEnquiryList(){
        return enquiryService.getUnReasolvedEnquiry();
    }

    // list all resolved enquiry
    @GetMapping("/enquiry/reasolved")
    public List<Enquiry> getReasolvedEnquiryList(){
        return enquiryService.getReasolvedEnquiry();
    }
    //getEnquiryByRole Agent
    @GetMapping("/enquiry/agent-role")
    public List<Enquiry> getAgentEnquiryList(){
        return enquiryService.getEnquiryByRole("Agent");
    }
    //getEnquiryByRole User
    @GetMapping("/enquiry/user-role")
    public List<Enquiry> getUserEnquiryList(){
        return enquiryService.getEnquiryByRole("User");
    }

    //getEnquiryByRole Guest
    @GetMapping("/enquiry/guest-role")
    public List<Enquiry> getGuestEnquiryList(){
        return enquiryService.getEnquiryByRole("Guest");
    }
    //getEnquiryByRoleAndResolution
    @GetMapping("/enquiry/pending-guest-enquiry")
    public List<Enquiry> getPendingGuestEnquiryList(){
        return enquiryService.getEnquiryByRoleAndResolution("Guest",false);
    }
    @GetMapping("/enquiry/resolved-guest-enquiry")
    public List<Enquiry> getResolvedGuestEnquiryList(){
        return enquiryService.getEnquiryByRoleAndResolution("Guest",true);
    }
    @GetMapping("/enquiry/resolved-user-enquiry")
    public List<Enquiry> getResolvedUserEnquiryList(){
        return enquiryService.getEnquiryByRoleAndResolution("User",true);
    }
    @GetMapping("/enquiry/pending-user-enquiry")
    public List<Enquiry> getPendingUserEnquiryList(){
        return enquiryService.getEnquiryByRoleAndResolution("User",false);
    }
    @GetMapping("/enquiry/resolved-agent-enquiry")
    public List<Enquiry> getResolvedAgentEnquiryList(){
        return enquiryService.getEnquiryByRoleAndResolution("Agent",true);
    }
    @GetMapping("/enquiry/pending-agent-enquiry")
    public List<Enquiry> getPendingAgentEnquiryList(){
        return enquiryService.getEnquiryByRoleAndResolution("Agent",false);
    }

    @GetMapping("/enquiry/current-user")
    public List<Enquiry> currentUserEnquiry(@RequestParam(required = false) HttpServletRequest request) {
        //Extract token from Authorization Header
        String header=request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token=header.substring(7);
        String username=jwtHelper.getUsernameFromToken(token);
        return enquiryService.getEnquiryByUsername(username);
    }

    //Update enquiry (mark resolved/unresolved + add comments)
    @PutMapping("/enquiry/update")
    public ResponseEntity<Enquiry> updateEnquiry(@RequestBody EnquiryUpdateDTO enquiryUpdateDTO) {
        Enquiry updatedEnquiry = enquiryService.updateEnquiry(enquiryUpdateDTO,"Admin","Admin");
        return ResponseEntity.ok(updatedEnquiry);
    }




}

