package com.GraminSchool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AppConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user1=User.builder().username("Mehdi1").password(passwordEncoder().encode("Mehdi1")).roles("ADMIN").build();
        UserDetails user2=User.builder().username("Mehdi2").password(passwordEncoder().encode("Mehdi2")).roles("ADMIN").build();
        UserDetails user3=User.builder().username("Mehdi3").password(passwordEncoder().encode("Mehdi2")).roles("AGENT").build();
        UserDetails user4=User.builder().username("Mehdi4").password(passwordEncoder().encode("Mehdi2")).roles("AGENT").build();
        UserDetails user5=User.builder().username("Mehdi5").password(passwordEncoder().encode("Mehdi2")).roles("USER").build();
        UserDetails user6=User.builder().username("Mehdi6").password(passwordEncoder().encode("Mehdi2")).roles("USER").build();

        return new InMemoryUserDetailsManager(user1,user2,user3,user4,user5,user6);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();

    }

}
