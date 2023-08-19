package com.tvntvn.letsplay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tvntvn.letsplay.filter.ExceptionFilter;
import com.tvntvn.letsplay.filter.JwtAuthFilter;
import com.tvntvn.letsplay.filter.RateLimitFilter;
import com.tvntvn.letsplay.service.UserService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
public class SecurityConfig {

  @Autowired private JwtAuthFilter authFilter;

  @Autowired private RateLimitFilter rateLimitFilter;

  @Autowired private UserService userService;

  @Autowired private ExceptionFilter exceptionFilter;

  @Value("${server.ssl.trust-store}")
  private String trustStore;

  @Value("${server.ssl.keystore}")
  private String keystore;

  @Value("${server.ssl.keystore-type}")
  private String keystoreType;

  @Value("${server.ssl.trust-store-type}")
  private String trustStoreType;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    System.out.println("keystore\n" + keystore);
    System.out.println("keystoreType\n" + keystoreType);
    System.out.println("trustStore\n" + trustStore);
    System.out.println("trustStoreType\n" + trustStoreType);
    return http.csrf(csrf -> csrf.disable())
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(exceptionFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/signup")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/products")
                    .permitAll()
                    .requestMatchers("/api/users", "/api/users/**", "/api/products/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll())
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
