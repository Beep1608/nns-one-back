package com.nns.punto_venta.modules.security.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nns.punto_venta.modules.security.services.JwtService;
import com.nns.punto_venta.modules.tenant.context.TenantContext;
import com.nns.punto_venta.modules.tenant.entities.CustomTenantDetail;
import com.nns.punto_venta.modules.tenant.services.TenantUserDetailImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
 
    private final TenantUserDetailImpl tenantUserDetailImpl; 

    public JwtAuthFilter(JwtService jwtService, TenantUserDetailImpl tenantUserDetailImpl) {
        this.jwtService = jwtService;
        this.tenantUserDetailImpl = tenantUserDetailImpl;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Validar que el header exista y tenga el formato correcto
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        
        try {
            // 2. Extraer claims
            final String username = jwtService.extractUsername(jwt);
            final Long tenantId = jwtService.extractTenantId(jwt);

            // 3. Validar que no estemos ya autenticados en este hilo
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // --- INICIO DE ZONA CRÍTICA MULTI-TENANT ---
                
                // 4. Establecer el TenantContext ANTES de buscar al usuario.
                TenantContext.setCurrentTenant(tenantId);

                // 5. Cargar detalles del usuario
                // El username en el token de un tenant es su email.
                CustomTenantDetail userDetails = (CustomTenantDetail) this.tenantUserDetailImpl.loadUserByUsername(username);

                // 6. Establecer el esquema del tenant para Hibernate
                TenantContext.setCurrentSchema(userDetails.getSchemaName());

                // 7. Validar token contra los detalles (fecha expiración, coincidencia de usuario)
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 8. Establecer el usuario en Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Error al procesar el JWT (token inválido, expirado, mal formado, usuario no encontrado, etc.)
            // Limpiamos el contexto por si acaso hubo un error parcial.
            SecurityContextHolder.clearContext();
            TenantContext.clear();
        }

        try {
            // 9. Continuar con la cadena de filtros hacia el Controller
            filterChain.doFilter(request, response);
        } finally {
            // 10. LIMPIEZA OBLIGATORIA al finalizar el request
            TenantContext.clear();
        }
    }
}