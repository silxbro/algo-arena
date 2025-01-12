package algo_arena.config.security;

import algo_arena.utils.jwt.service.JwtRequestFilter;
import algo_arena.utils.jwt.service.JwtUserDetailsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalAuthentication
public class WebSecurityConfig {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;

    //전역 인증 설정 (userDetailsService 와 비밀번호 인코더를 설정)
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    //HTTP 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) //CORS 설정 적용
            .csrf(AbstractHttpConfigurer::disable) //CSRF 보호 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/email-auth/**").permitAll() //인증 없이 접근 허용
                .anyRequest().authenticated() //기타 요청은 인증 필요
            )
            .exceptionHandling(exceptionConfigurer -> exceptionConfigurer
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //미인증 처리
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 생성 비활성화(상태 비저장 정책, JWT 사용)
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); //커스텀 인증 필터 추가

        return httpSecurity.build();
    }

    //CORS 설정: 모든 출처, 모든 메서드, 모든 헤더를 허용하도록 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // 모든 출처 허용
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 URL 에 대해 CORS 설정 적용
        return source;
    }

    //AuthenticationManager 빈 생성 (Spring Security 인증 매니저)
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}