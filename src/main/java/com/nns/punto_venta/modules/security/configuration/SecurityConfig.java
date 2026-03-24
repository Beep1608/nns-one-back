package com.nns.punto_venta.modules.security.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nns.punto_venta.modules.security.filters.JwtAuthFilter;
import com.nns.punto_venta.modules.security.services.UserDetailsImpl;
import com.nns.punto_venta.modules.tenant.services.TenantUserDetailImpl;


import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsImpl userDetailsImpl;
    private final ApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SecurityConfig(UserDetailsImpl userDetailsImpl , ApplicationContext context)
    {
        this.userDetailsImpl = userDetailsImpl;
        this.context = context;

    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Usuario no autenticado o sesión expirada.");
            data.put("error", authException.getMessage());
            response.getOutputStream().println(objectMapper.writeValueAsString(data));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Acceso denegado: No tiene los permisos necesarios.");
            data.put("error", accessDeniedException.getMessage());
            response.getOutputStream().println(objectMapper.writeValueAsString(data));
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean 
    public DaoAuthenticationProvider tenantAuthProvider(TenantUserDetailImpl tenantUserDetailImpl){
       DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(tenantUserDetailImpl);
       authenticationProvider.setPasswordEncoder(passwordEncoder());
       return authenticationProvider;
    }

    @Bean

    public DaoAuthenticationProvider usersAuthenticationProvider(){
       DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsImpl);
       authenticationProvider.setPasswordEncoder(passwordEncoder());
       return authenticationProvider;
    }


    @Bean("tenantAuthManager")
    @Primary
    public AuthenticationManager tenantAuthManager(TenantUserDetailImpl tenantUserDetailImpl) {
       return new ProviderManager(tenantAuthProvider(tenantUserDetailImpl));
    }

    @Bean("userAuthManager")
    public AuthenticationManager userAuthManager() {
       return new ProviderManager(usersAuthenticationProvider());
    }

     @Bean
     @Order(1)
     @Description("Chain used fo authentication of tenants")
     public SecurityFilterChain tenantLoginFilterChain(HttpSecurity http, TenantUserDetailImpl tenantUserDetailImpl) throws Exception {
         http
             .securityMatcher("/api/tenants/login") 
             .csrf(csrf -> csrf.disable())
             //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             //.authenticationManager(tenantAuthManager(tenantUserDetailImpl))
             //.httpBasic(Customizer.withDefaults())
             .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
             
    
         return http.build();
     }
    @Bean
    @Order(2)
    public SecurityFilterChain jwtResourcesFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/tenants/register").permitAll() 
                .requestMatchers(HttpMethod.POST, "/api/debug/password-encode").permitAll() 
                .requestMatchers(HttpMethod.POST, "/api/debug/debug").permitAll() 
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
