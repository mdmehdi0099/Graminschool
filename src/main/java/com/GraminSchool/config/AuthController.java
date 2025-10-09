package com.GraminSchool.config;

import com.GraminSchool.entity.User;
import com.GraminSchool.dto.JwtRequest;
import com.GraminSchool.models.JwtResponse;
import com.GraminSchool.models.RegisterResponse;
import com.GraminSchool.security.JwtHelper;
import com.GraminSchool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper jwtHelper;

    private final UserService userService;

    private Logger logger=LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login1(@RequestBody JwtRequest request){
//        this.doAuthenticate(request.getEmail(),request.getPassword());
//        UserDetails userDetails=userDetailsService.loadUserByUsername(request.getEmail());
//        String token=this.jwtHelper.generateToken(userDetails);
//
//        JwtResponse response=JwtResponse.builder()
//                .jwtToken(token)
//                .username(userDetails.getUsername()).build();
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//
//    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        JwtResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }




    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody User request) {
        RegisterResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    //registerAdminUser
    @PostMapping("/registerAdminUser")
    public ResponseEntity<RegisterResponse> registerAdminUser(@RequestBody User request) {
        RegisterResponse response = userService.registerAdminUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }






    private void doAuthenticate(String email,String password){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(email,password);

        try {
            manager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid Username or Password !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String  exceptionHandler(){
        return "Invalid Credential !!!!!";
    }







}
