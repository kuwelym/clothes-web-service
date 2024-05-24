package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request ->
                        request.requestMatchers("**/auth/**").permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "**/products/**",
                                        "**/categories/**",
                                        "**/product-images/**",
                                        "**/product-quantities/**",
                                        "**/colors/**",
                                        "**/sizes/**"
                                ).permitAll()
                                .requestMatchers("**/favorite-products/**","**/carts/**","**/orders/**").hasAuthority("USER")
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "**/products/**",
                                        "**/categories/**",
                                        "**/product-images/**",
                                        "**/product-quantities/**",
                                        "**/colors/**",
                                        "**/sizes/**",
                                        "**/orders/**"
                                ).hasAuthority("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "**/products/**",
                                        "**/categories/**",
                                        "**/product-images/**",
                                        "**/product-quantities/**",
                                        "**/colors/**",
                                        "**/sizes/**"
                                ).hasAuthority("ADMIN")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "**/products/**",
                                        "**/categories/**",
                                        "**/product-images/**",
                                        "**/product-quantities/**",
                                        "**/colors/**",
                                        "**/sizes/**",
                                        "**/orders/**"
                                ).hasAuthority("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "**/orders/**"
                                ).hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "**/orders/**"
                                ).hasAnyAuthority("ADMIN", "USER")
                                .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
