package ru.satird.pcs.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.satird.pcs.dto.UserDetailsImpl;

import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${satird.app.jwtSecret}")
    private String jwtSecret;
    @Value("${satird.app.jwtExpirationMs}")
    private Integer jwtExpirationMs;

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getEmail());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (ExpiredJwtException expEx) {
            logger.error("JWT token is expired: {}", expEx.getMessage());
        } catch (UnsupportedJwtException unsEx) {
            logger.error("JWT token is unsupported: {}", unsEx.getMessage());
        } catch (MalformedJwtException mjEx) {
            logger.error("Invalid JWT token: {}", mjEx.getMessage());
        } catch (SignatureException sEx) {
            logger.error("Invalid JWT signature: {}", sEx.getMessage());
        } catch (IllegalArgumentException ilEx){
            logger.error("JWT claims string is empty: {}", ilEx.getMessage());
        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage());
        }
        return false;
    }
}
