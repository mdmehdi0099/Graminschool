package com.GraminSchool.controllers;

import com.GraminSchool.entity.User;
import com.GraminSchool.models.RegisterResponse;
import com.GraminSchool.models.admin.UserRegistrationResponse;
import com.GraminSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

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




}
