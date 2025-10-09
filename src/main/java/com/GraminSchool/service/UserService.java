package com.GraminSchool.service;


import com.GraminSchool.entity.Role;
import com.GraminSchool.entity.User;
import com.GraminSchool.dto.JwtRequest;
import com.GraminSchool.models.JwtResponse;
import com.GraminSchool.models.RegisterResponse;
import com.GraminSchool.models.admin.FailedUserRow;
import com.GraminSchool.models.admin.UserRegistrationResponse;
import com.GraminSchool.repo.UserRepository;
import com.GraminSchool.security.JwtHelper;
import com.GraminSchool.utility.ExcelCsvHelperUserRegistration;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final JwtHelper jwtHelper;

    //private List<User> user=new ArrayList<>();
//    public UserService(){
//        user.add(new User(UUID.randomUUID().toString(),"Mehdi Hassan 1","1mdmehdi0099@gmail.com"));
//        user.add(new User(UUID.randomUUID().toString(),"Mehdi Hassan 2","2mdmehdi0099@gmail.com"));
//        user.add(new User(UUID.randomUUID().toString(),"Mehdi Hassan 3","3mdmehdi0099@gmail.com"));
//        user.add(new User(UUID.randomUUID().toString(),"Mehdi Hassan 4","4mdmehdi0099@gmail.com"));
//        passwordEncoder = null;
//    }


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtHelper jwtHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper=jwtHelper;
    }
    public RegisterResponse registerAdminUser(User request) {
        // check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // encrypt password
        //check the role
        String role= String.valueOf(request.getRole());
        if (role.equalsIgnoreCase("ADMIN")){
            //throw new IllegalArgumentException("Registration as ADMIN is restricted");
            user.setRole(request.getRole());
        }else{
            user.setRole(request.getRole());
        }
        user.setAddress(request.getAddress());
        user.setStatus(request.isStatus());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser=userRepository.save(user);

        //build UserDetails for token generation
        UserDetails userDetails=org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getEmail())
                .password(savedUser.getPassword())
                .roles(savedUser.getRole().name())
                .build();

        String token=jwtHelper.generateToken(userDetails);
        return new RegisterResponse(token,savedUser.getEmail(),savedUser.getRole().name());
    }
    public RegisterResponse registerUser(User request) {
        // check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // encrypt password
        //check the role
        String role= String.valueOf(request.getRole());
        if (role.equalsIgnoreCase("ADMIN")){
            throw new IllegalArgumentException("Registration as ADMIN is restricted");
        }else{
            user.setRole(request.getRole());
        }
        user.setAddress(request.getAddress());
        user.setStatus(request.isStatus());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser=userRepository.save(user);

        //build UserDetails for token generation
        UserDetails userDetails=org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getEmail())
                .password(savedUser.getPassword())
                .roles(savedUser.getRole().name())
                .build();

        String token=jwtHelper.generateToken(userDetails);
        return new RegisterResponse(token,savedUser.getEmail(),savedUser.getRole().name());
    }

    @Transactional
    public UserRegistrationResponse registerNormalUser(User request) {
        // check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // encrypt password
        user.setRole(Role.USER);
        user.setAddress(request.getAddress());
        user.setStatus(request.isStatus());
        user.setCreatedAt(LocalDateTime.now());
        User savedUser=userRepository.save(user);

        return new UserRegistrationResponse(savedUser.getEmail(),savedUser.getRole().name());
    }
    @Transactional
    public UserRegistrationResponse registerAgentUser(User request) {
        // check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // encrypt password
        user.setRole(Role.AGENT);
        user.setAddress(request.getAddress());
        user.setStatus(request.isStatus());
        user.setCreatedAt(LocalDateTime.now());
        User savedUser=userRepository.save(user);

        return new UserRegistrationResponse(savedUser.getEmail(),savedUser.getRole().name());
    }
    //bulk user registration by admin
    public Map<String, Object> saveUsersFromFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        List<User> users = new ArrayList<>();
        List<FailedUserRow> failedRows = new ArrayList<>();

        if (filename.endsWith(".xlsx")) {
            try {
                users = ExcelCsvHelperUserRegistration.parseExcel(file.getInputStream());
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException("Error while parsing Excel file: " + e.getMessage());
            }
        } else if (filename.endsWith(".csv")) {
            try {
                users = ExcelCsvHelperUserRegistration.parseCSV(file.getInputStream());
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException("Error while parsing CSV file: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only .xlsx,.csv are allowed.");
        }

        // Validate each user before saving
        int rowNumber = 1; // start after header row
        List<User> validUsers = new ArrayList<>();

        for (User user : users) {
            try {
                if (user.getName() == null || user.getName().isEmpty())
                    throw new IllegalArgumentException("Name is missing");
                if (user.getEmail() == null || user.getEmail().isEmpty())
                    throw new IllegalArgumentException("Email is missing");
                if (user.getPhone() == null || user.getPhone().isEmpty())
                    throw new IllegalArgumentException("Phone is missing");
                if (user.getRole() == null)
                    throw new IllegalArgumentException("Role is missing");
                if (user.getAddress() == null || user.getAddress().isEmpty())
                    throw new IllegalArgumentException("Address is missing");
                if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                    throw new IllegalArgumentException("Admin user registration is restricted");
                }

                // Passed validation → keep user for saving
                validUsers.add(user);

            } catch (Exception e) {
                failedRows.add(new FailedUserRow(
                        rowNumber + 1,
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole() != null ? user.getRole().toString() : null,
                        user.getAddress(),
                        String.valueOf(user.isStatus()),
                        e.getMessage()
                ));
            }
            rowNumber++;
        }

        // 🔹 Step 1: If validation failed, stop and return only failed rows
        if (!failedRows.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("failedRows", failedRows);
            result.put("message", "Some records failed validation. Please fix and reupload.");
            return result;
        }

        // 🔹 Step 2: Encrypt passwords
        validUsers.forEach(user ->
                user.setPassword(passwordEncoder.encode(user.getPassword()))
        );

        // 🔹 Step 3: Try saving users one by one to catch DB-level errors (like duplicate email/phone)
        List<User> savedUsers = new ArrayList<>();
        for (int i = 0; i < validUsers.size(); i++) {
            User user = validUsers.get(i);
            try {
                User saved = userRepository.save(user);
                savedUsers.add(saved);
            } catch (Exception e) {
                failedRows.add(new FailedUserRow(
                        i + 2, // Excel row number (header skipped)
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole() != null ? user.getRole().toString() : null,
                        user.getAddress(),
                        String.valueOf(user.isStatus()),
                        "Database error: " + e.getMessage()
                ));
            }
        }

        // 🔹 Step 4: Return appropriate result
        Map<String, Object> result = new HashMap<>();
        if (!failedRows.isEmpty()) {
            result.put("success", false);
            result.put("failedRows", failedRows);
            result.put("message", "Some records failed during save. Please review errors.");
        } else {
            result.put("success", true);
            result.put("savedUsers", savedUsers);
            result.put("message", "All users registered successfully.");
        }

        return result;
    }

    //bulk normal user registration by admin
    public Map<String, Object> saveUsersFromFileForNormalUser(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        List<User> users = new ArrayList<>();
        List<FailedUserRow> failedRows = new ArrayList<>();

        if (filename.endsWith(".xlsx")) {
            try {
                users = ExcelCsvHelperUserRegistration.parseExcelForNormalUser(file.getInputStream());
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException("Error while parsing Excel file: " + e.getMessage());
            }
        } else if (filename.endsWith(".csv")) {
            try {
                users = ExcelCsvHelperUserRegistration.parseCSV(file.getInputStream());
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException("Error while parsing CSV file: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only .xlsx,.csv are allowed.");
        }

        // Validate each user before saving
        int rowNumber = 1; // start after header row
        List<User> validUsers = new ArrayList<>();

        for (User user : users) {
            try {
                if (user.getName() == null || user.getName().isEmpty())
                    throw new IllegalArgumentException("Name is missing");
                if (user.getEmail() == null || user.getEmail().isEmpty())
                    throw new IllegalArgumentException("Email is missing");
                if (user.getPhone() == null || user.getPhone().isEmpty())
                    throw new IllegalArgumentException("Phone is missing");
                if (user.getRole() == null)
                    throw new IllegalArgumentException("Role is missing");
                if (user.getAddress() == null || user.getAddress().isEmpty())
                    throw new IllegalArgumentException("Address is missing");
                if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                    throw new IllegalArgumentException("Admin user registration is restricted");
                }

                // Passed validation → keep user for saving
                validUsers.add(user);

            } catch (Exception e) {
                failedRows.add(new FailedUserRow(
                        rowNumber + 1,
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole() != null ? user.getRole().toString() : null,
                        user.getAddress(),
                        String.valueOf(user.isStatus()),
                        e.getMessage()
                ));
            }
            rowNumber++;
        }

        // 🔹 Step 1: If validation failed, stop and return only failed rows
        if (!failedRows.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("failedRows", failedRows);
            result.put("message", "Some records failed validation. Please fix and reupload.");
            return result;
        }

        // 🔹 Step 2: Encrypt passwords
        validUsers.forEach(user ->
                user.setPassword(passwordEncoder.encode(user.getPassword()))
        );

        // 🔹 Step 3: Try saving users one by one to catch DB-level errors (like duplicate email/phone)
        List<User> savedUsers = new ArrayList<>();
        for (int i = 0; i < validUsers.size(); i++) {
            User user = validUsers.get(i);
            try {
                User saved = userRepository.save(user);
                savedUsers.add(saved);
            } catch (Exception e) {
                failedRows.add(new FailedUserRow(
                        i + 2, // Excel row number (header skipped)
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole() != null ? user.getRole().toString() : null,
                        user.getAddress(),
                        String.valueOf(user.isStatus()),
                        "Database error: " + e.getMessage()
                ));
            }
        }

        // 🔹 Step 4: Return appropriate result
        Map<String, Object> result = new HashMap<>();
        if (!failedRows.isEmpty()) {
            result.put("success", false);
            result.put("failedRows", failedRows);
            result.put("message", "Some records failed during save. Please review errors.");
        } else {
            result.put("success", true);
            result.put("savedUsers", savedUsers);
            result.put("message", "All users registered successfully.");
        }

        return result;
    }




    public JwtResponse loginUser(JwtRequest request) {
        // 1. Find user by email (username)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. Check if status is true
        if (!user.isStatus()) {  // <--- this checks DB column `status`
            throw new RuntimeException("User account is inactive. Please contact admin.");
        }

        // 2. Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Build UserDetails for JWT
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // already BCrypt hashed
                .roles(user.getRole().name())
                .build();

        // 4. Generate token
        String token = jwtHelper.generateToken(userDetails);

        // 5. Return response
        return new JwtResponse(token, user.getName());
    }




    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public List<User> getAgents(){
        return userRepository.findAll().stream().filter(user -> user.getRole()==Role.AGENT).toList();
    }
    public List<User> getNormalUser(){
        return userRepository.findAll().stream().filter(user -> user.getRole()==Role.USER).toList();
    }




}
