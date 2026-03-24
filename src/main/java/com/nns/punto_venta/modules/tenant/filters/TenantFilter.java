package com.nns.punto_venta.modules.tenant.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nns.punto_venta.modules.tenant.context.TenantContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
                String tenantId = request.getHeader("X-Tenant-ID");
                if(tenantId !=null){
                    TenantContext.setCurrentTenant(Long.parseLong(tenantId));
                }

                try{
                    filterChain.doFilter(request, response);
                }finally{
                    TenantContext.clear();
                }
    }
    
}
