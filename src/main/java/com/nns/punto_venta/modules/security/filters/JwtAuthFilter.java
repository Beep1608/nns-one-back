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
import com.nns.punto_venta.modules.tenant.services.TenantUserDetailImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    // Inyecta el UserDetailsService que lee de los esquemas de los tenants
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
            // 2. Extraer claims (Aquí asumimos que tu JwtService lanza excepciones si expira/es inválido)
            final String username = jwtService.extractUsername(jwt);
            final String tenantId = jwtService.extractTenantId(jwt); // claim personalizado

            // 3. Validar que no estemos ya autenticados en este hilo
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // --- INICIO DE ZONA CRÍTICA MULTI-TENANT ---
                
                // 4. Establecer el TenantContext ANTES de buscar al usuario.
                // Esto garantiza que Hibernate sepa a qué esquema ir en la siguiente línea.
                TenantContext.setCurrentTenant(tenantId);

                // 5. Cargar detalles del usuario (opcional si haces el token 100% stateless, ver notas abajo)
                UserDetails userDetails = this.tenantUserDetailImpl.loadUserByUsername(username);

                // 6. Validar token contra los detalles (fecha expiración, coincidencia de usuario)
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 7. Establecer el usuario en Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            
            // 8. Continuar con la cadena de filtros hacia el Controller
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Manejar excepciones de JWT (ej. ExpiredJwtException, SignatureException)
            // En un entorno profesional, aquí devuelves un 401 estructurado (JSON) en el response.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalido o expirado");
        } finally {
            // --- FIN DE ZONA CRÍTICA MULTI-TENANT ---
            
            // 9. LIMPIEZA OBLIGATORIA (Prevención de fugas de datos cruzados)
            // Tomcat utiliza un Thread Pool. Si no limpias esto, el hilo se reciclará 
            // para otra petición manteniendo el tenantId anterior.
            TenantContext.clear();
        }
    }
}