package com.nns.punto_venta.modules.security.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nns.punto_venta.modules.security.filters.JwtAuthFilter;
import com.nns.punto_venta.modules.security.services.UserDetailsImpl;
import com.nns.punto_venta.modules.tenant.services.TenantUserDetailImpl;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsImpl userDetailsImpl;
    private final TenantUserDetailImpl tenantUserDetailImpl;
    private final ApplicationContext context;
    public SecurityConfig(UserDetailsImpl userDetailsImpl, TenantUserDetailImpl tenantUserDetailImpl , ApplicationContext context)
    {
        this.userDetailsImpl = userDetailsImpl;
        this.tenantUserDetailImpl = tenantUserDetailImpl;
        this.context = context;

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean 
    public DaoAuthenticationProvider tenantAuthProvider(){
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
    public AuthenticationManager tenantAuthManager() {
        return new ProviderManager(tenantAuthProvider());
    }

    @Bean("userAuthManager")
    public AuthenticationManager userAuthManager() {
        return new ProviderManager(usersAuthenticationProvider());
    }

    @Bean
    @Order(1)
    public SecurityFilterChain tenantLoginFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/tenants/login") // SOLO aplica a esta ruta exacta
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults()) // Habilita Basic Auth solo aquí
            .authenticationManager(tenantAuthManager()); // Usa el manager global de tenants

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
                .anyRequest().authenticated()
            )
          
            .httpBasic(basic -> basic.disable()) 
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
