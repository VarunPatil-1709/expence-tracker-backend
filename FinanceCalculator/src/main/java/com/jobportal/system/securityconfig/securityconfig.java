package com.jobportal.system.securityconfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jobportal.system.eventProducer.UserInfoProducer;
import com.jobportal.system.repo.UserRepository;
import com.jobportal.system.service.UserDetailsServiceImp;

@Configuration
@EnableMethodSecurity
@Data
public class securityconfig {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserDetailsServiceImp userDetailsServiceImpl;

    @Autowired
    private final UserInfoProducer userInfoProducer;

    @Bean
    @Autowired
    public UserDetailsService userDetailsService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return new UserDetailsServiceImp(userRepository, passwordEncoder, userInfoProducer);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(CorsConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // ✅ AUTH APIs
                        .requestMatchers(
                                "/auth/v1/login",
                                "/auth/v1/refreshToken",
                                "/auth/v1/signup"
                        ).permitAll()

                        // ✅ SWAGGER APIs
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()

                        // 🔒 Everything else secured
                        .anyRequest().authenticated()
                )

                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ❌ Disable basic auth popup (recommended for JWT)
                .httpBasic(AbstractHttpConfigurer::disable)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
