package com.cleantracksystem.cleantrack.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Auth is entirely JWT-based (JwtAuthFilter populates the SecurityContext
    // directly) - this exists only to stop Spring Boot's auto-configuration
    // from standing up an in-memory user with a random generated password.
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("Username/password auth is not supported");
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Spring Security 6's AuthorizationFilter runs on every dispatch
                        // type by default, including the internal ERROR forward Spring
                        // Boot uses to render a failed request's error response. Without
                        // this, ANY unhandled exception (a bad SQL insert, a bug, etc.)
                        // gets re-rejected as 403 by this same rule chain before it ever
                        // reaches the client as the real error - which the frontend then
                        // misreads as "not authenticated" and logs the admin out.
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        // Bootstrap / login never require a token.
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/admins-exist")
                        .permitAll()
                        .requestMatchers("/health", "/api/health").permitAll()
                        // Uploaded issue photos are served as plain static files.
                        .requestMatchers("/uploads/**").permitAll()
                        // Cleaners have no accounts - the whole checklist flow (read the
                        // roster/shifts/items/floors/areas, submit a record + its
                        // tasks/issues, upload a photo) must work without a token.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cleaners/**",
                                "/api/shifts/**",
                                "/api/cleaning-items/**",
                                "/api/floors/**",
                                "/api/areas/**",
                                "/api/floor-area-items/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/cleaning-records", "/api/record-tasks", "/api/issues")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/uploads/issue-photos").permitAll()
                        // Everything else (admin reads/writes, admin management) needs a
                        // valid token for an active admin.
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
