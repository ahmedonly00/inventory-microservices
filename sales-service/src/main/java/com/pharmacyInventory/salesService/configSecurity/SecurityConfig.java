package com.pharmacyInventory.salesService.configSecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final EmailVerificationFilter emailVerificationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write(
                            String.format("{\"status\": %d, \"error\": \"Unauthorized\", \"message\": \"%s\"}",
                                HttpServletResponse.SC_UNAUTHORIZED,
                                "Authentication failed: " + authException.getMessage())
                        );
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write(
                            String.format("{\"status\": %d, \"error\": \"Forbidden\", \"message\": \"%s\"}",
                                HttpServletResponse.SC_FORBIDDEN,
                                "Access Denied: " + accessDeniedException.getMessage())
                        );
                    })
            )
            .authorizeHttpRequests(authorize -> 
                authorize
                    .requestMatchers(
                        "/api/auth/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/error",
                        "/favicon.ico"
                    ).permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    // Public endpoints - no authentication required
                    .requestMatchers(
                        "/api/medications/getAllMedications",
                        "/api/medications/getMedicationById/**",
                        "/api/medications/search",
                        "/api/categories/getAllCategories",
                        "/api/categories/getCategoryById/**",
                        "/api/suppliers/getAllSuppliers",
                        "/api/suppliers/getSupplierById/**"
                    ).permitAll()
                    // Admin and Pharmacist endpoints - require ADMIN or PHARMACIST role
                    .requestMatchers(
                        "/api/users/**",
                        "/api/medications/**",
                        "/api/categories/**",
                        "/api/suppliers/**",
                        "/api/sales/**",
                        "/api/transactions/**",
                        "/api/transfers/**",
                        "/api/taxes/**",
                        "/api/settings/**",
                        "/api/bulk/**"
                    ).hasAnyRole("ADMIN", "PHARMACIST")
                    // Dashboard and Reports - require ADMIN, PHARMACIST, or MANAGER role
                    .requestMatchers(
                        "/api/dashboard/**",
                        "/api/reports/**"
                    ).hasAnyRole("ADMIN", "PHARMACIST", "MANAGER")
                    // Audit Logs - require ADMIN role only
                    .requestMatchers(
                        "/api/audit-logs/**"
                    ).hasRole("ADMIN")
                    // Notifications and Alerts - accessible by all authenticated users
                    .requestMatchers(
                        "/api/notifications/**",
                        "/api/alerts/**"
                    ).hasAnyRole("ADMIN", "PHARMACIST", "MANAGER", "CASHIER")
                    // Equivalents - require PHARMACIST or ADMIN role
                    .requestMatchers(
                        "/api/equivalents/**"
                    ).hasAnyRole("PHARMACIST", "ADMIN")
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(emailVerificationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
