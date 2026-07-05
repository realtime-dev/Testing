package com.example.authdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    // Primary in-memory user: username=kappa, password=TEST2 (dev only)
    @Bean
    @Primary
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("kappa")
            .password(passwordEncoder.encode("TEST2"))
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Force AuthenticationManager to use the in-memory userDetailsService + encoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       InMemoryUserDetailsManager userDetailsService) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(
                  new AntPathRequestMatcher("/register"),
                  new AntPathRequestMatcher("/login"),
                  new AntPathRequestMatcher("/css/**"),
                  new AntPathRequestMatcher("/h2-console/**")
              ).permitAll()
              .anyRequest().authenticated()
          )
          .formLogin(form -> form
              .loginPage("/login")
              .failureUrl("/login?error")          // explicit failure URL
              .defaultSuccessUrl("/dashboard", true)
              .permitAll()
          )
          .logout(logout -> logout
              // default expects POST to /logout with CSRF token
              .logoutUrl("/logout")
              .logoutSuccessUrl("/login?logout")
              .permitAll()
          )
          .csrf(csrf -> csrf
              .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
          )
          .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }
}