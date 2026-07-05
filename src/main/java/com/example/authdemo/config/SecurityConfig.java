package com.example.authdemo.config;

import com.example.authdemo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider prov = new DaoAuthenticationProvider();
        prov.setUserDetailsService(userDetailsService);
        prov.setPasswordEncoder(passwordEncoder());
        return prov;
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
          .authenticationProvider(authenticationProvider())
          .formLogin(form -> form
              .loginPage("/login")
              .defaultSuccessUrl("/dashboard", true)
              .permitAll()
          )
          .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
          .csrf(csrf -> csrf
              .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
          )
          .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }
}