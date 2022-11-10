package com.marcin.springboot_react.configuration;

import com.marcin.springboot_react.model.UserLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private AuthenticationEntryPoint jwtUnauthorizedEntryPoint;

    @Bean
    protected SecurityFilterChain addAuthFilter(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .exceptionHandling().authenticationEntryPoint(this.jwtUnauthorizedEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/login", "/auth/create-account").permitAll()
                .antMatchers("/healthcheck", "/").permitAll()
                .antMatchers("/products/list", "/products/add", "/products/print")
                .hasAnyRole(UserLevel.USER.toString(), UserLevel.ADMIN.toString())
                .antMatchers("/users").hasRole(UserLevel.ADMIN.toString())
                .and().formLogin().loginPage("/login").permitAll();

        http.addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
