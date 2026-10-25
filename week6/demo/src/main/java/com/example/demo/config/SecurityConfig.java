package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링 설정 클래스 등록
@EnableWebSecurity // 스프링 시큐리티 활성화
@EnableMethodSecurity // [6주차] 메서드 보안 활성화 : @PreAuthorize 사용
public class SecurityConfig {

    @Bean // 비밀번호 암호화 객체 등록 (BCrypt 해시)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 보안 필터 체인 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth // 1. URL 접근 규칙 (인가)
                .requestMatchers("/", "/hello", "/hello2", "/login", "/signup", "/error").permitAll()
                .requestMatchers("/detailed_web.html", "/detailed_ai.html", "/detailed_security.html", "/detailed_data.html").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN") // [6주차] 관리자만 (ROLE_ADMIN)
                .requestMatchers("/testdb").hasAnyRole("ADMIN", "MANAGER") // [6주차 연습문제 ②] 회원목록은 관리자·매니저만
                .anyRequest().authenticated()) // 나머지(/mypage 등) : 로그인 필요. 항상 마지막
            .formLogin(form -> form // 2. 폼 로그인 설정 (인증)
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll())
            .logout(logout -> logout // 3. 로그아웃 설정
                .logoutUrl("/logout") // 로그아웃 처리 URL (POST)
                .logoutSuccessUrl("/login?logout") // 로그아웃 후 이동
                .invalidateHttpSession(true) // 세션 삭제
                .deleteCookies("JSESSIONID", "remember-me")) // 쿠키 삭제
            .rememberMe(remember -> remember // 4. 5주차 연습문제 ①: 로그인 상태 유지 (login.html 체크박스 name="remember-me")
                .key("moodmusic-week5-remember-key") // 쿠키 서명용 비밀키
                .tokenValiditySeconds(7 * 24 * 60 * 60)); // 7일
        return http.build();
    }
}
