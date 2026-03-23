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

import com.nns.punto_venta.modules.security.filters.JwtAuthFilter;
import com.nns.punto_venta.modules.security.services.UserDetailsImpl;
import com.nns.punto_venta.modules.tenant.services.TenantUserDetailImpl;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsImpl userDetailsImpl;
    private final ApplicationContext context;
    public SecurityConfig(UserDetailsImpl userDetailsImpl , ApplicationContext context)
    {
        this.userDetailsImpl = userDetailsImpl;
        this.context = context;

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
                .anyRequest().authenticated()
            )
          
            .httpBasic(basic -> basic.disable()) ;
           // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
