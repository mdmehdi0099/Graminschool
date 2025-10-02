package com.GraminSchool.service;


import com.GraminSchool.entity.Role;
import com.GraminSchool.entity.User;
import com.GraminSchool.models.JwtRequest;
import com.GraminSchool.models.JwtResponse;
import com.GraminSchool.models.RegisterResponse;
import com.GraminSchool.models.admin.UserRegistrationResponse;
import com.GraminSchool.repo.UserRepository;
import com.GraminSchool.security.JwtHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        user.setRole(request.getRole());
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
