package com.GraminSchool.service;

import com.GraminSchool.dto.ForgetPasswordResponseDTO;
import com.GraminSchool.entity.User;
import com.GraminSchool.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final UserRepository userRepository;

    public EmailService(JavaMailSender mailSender,UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository=userRepository;
    }

    public void sendRegistrationEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration Successful");
        message.setText("Hello " + userName + ",\n\nWelcome! Your registration was successful.\n\nRegards,\nTeam");

        mailSender.send(message);
    }

    @Transactional
    public ForgetPasswordResponseDTO sendForgetPasswordEmail(String toEmail) {
        // ✅ 1. Check if user exists
        Optional<User> userOptional = userRepository.findByEmail(toEmail);
        if (userOptional.isEmpty()) {
            return ForgetPasswordResponseDTO.builder()
                    .Email(toEmail)
                    .Message("User with email " + toEmail + " not found.")
                    .build();
        }

        User user = userOptional.get();

        // ✅ 2. Generate a random 6-digit OTP
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);

        // ✅ 3. Store OTP in the user table
        user.setOtp(code);
        userRepository.save(user);

        // ✅ 4. Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Forgot Password - OTP Verification");
        message.setText("Hello " + user.getName() + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Your OTP for password reset is: " + code + "\n\n"
                + "This OTP will expire in 10 minutes.\n\n"
                + "Regards,\nTeam GraminSchool");

        mailSender.send(message);

        // ✅ 5. Return response
        return ForgetPasswordResponseDTO.builder()
                .Email(toEmail)
                .Message("OTP sent successfully to " + toEmail)
                .build();
    }



}
