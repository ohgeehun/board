package com.ogh.board.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ogh.board.user.UserSecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    private final UserSecurityService userSecurityService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> 
                auth.requestMatchers("/**").permitAll()).csrf(csrf -> csrf.disable())
                .headers(headers -> 
                headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .formLogin(form -> 
                form.loginPage("/user/login").defaultSuccessUrl("/"))
                .logout(logout -> 
                logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true));
        // http.authorizeHttpRequests(auth ->
        // auth.requestMatchers("/**").permitAll()).csrf(csrf ->
        // csrf.ignoringRequestMatchers("/h2-console/**"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    // AuthenticationManagerBuilder는 Spring Security에서 인증을 구성하는 데 사용되는 빌더 클래스입니다.
    // 이 클래스는 사용자 인증 정보를 설정하고, 인증 프로세스를 구성하는 데 필요한 다양한 메서드를 제공합니다.
    // AuthenticationManagerBuilder는 사용자 인증을 위한 다양한 설정을 포함하고 있으며, 애플리케이션의 보안을 강화하는 데 기여합니다.
    }

}
