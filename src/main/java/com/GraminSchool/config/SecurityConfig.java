package com.GraminSchool.config;

import com.GraminSchool.security.JwtAuthenticationEntryPoint;
import com.GraminSchool.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter filter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        //configuration
        http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(
                        auth->
                                auth.requestMatchers("/home/**").authenticated()
                                        .requestMatchers("/auth/**").permitAll()
                                        .requestMatchers("/free/**").permitAll()
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/agent/**").hasAnyRole("AGENT","ADMIN")
                                        .requestMatchers("/user/**").hasAnyRole("USER","AGENT","ADMIN")
                                .anyRequest().authenticated())
                .exceptionHandling(ex->ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
