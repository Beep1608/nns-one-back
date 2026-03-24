package com.nns.punto_venta.modules.security.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nns.punto_venta.modules.tenant.entities.CustomTenantDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    
    @Value("${security.jwt.secret-key:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    @Value("${security.jwt.expiration-time:86400000}")
    private long jwtExpiration;



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // 2. NUEVO: método genérico simplificado para extraer por llave y tipo de clase
    public <T> T extractClaim(String token, String claimKey, Class<T> claimType) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimKey, claimType);
    }

    // 3. extracción del tenant_id. 
    // como en tu base de datos el id es BIGINT, en java debe ser Long.
    // JJWT a veces parsea números como Integer, por lo que usamos Number para ser seguros.
    public Long extractTenantId(String token) {
        Object tenantId = extractClaim(token, "tenantId", Object.class);
        if (tenantId == null) return null;
        if (tenantId instanceof Number) {
            return ((Number) tenantId).longValue();
        }
        try {
            return Long.valueOf(tenantId.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public String generateTokenTenant(CustomTenantDetail tenantDetail) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("tenantId", tenantDetail.getId()); 
        List<String> authorities = tenantDetail.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
        
        extraClaims.put("authorities", authorities);
        return buildToken(extraClaims, tenantDetail.getUsername(), jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String username, long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }



    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}