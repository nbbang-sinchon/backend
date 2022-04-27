package nbbang.com.nbbang.global.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final SecurityPolicy securityPolicy;

    public String createTokenByMemberId(Long memberId) {
        Date now = new Date(); // Time unit: Seconds
        Date expiryDate = new Date(now.getTime() + securityPolicy.getTokenExpireTime());
        return Jwts.builder()
                .setSubject(Long.toString(memberId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, securityPolicy.getTokenSecretKey())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(securityPolicy.getTokenSecretKey())
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        log.info("Validating token...");
        try {
            Jwts.parser().setSigningKey(securityPolicy.getTokenSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        log.info("Token is invalid.");
        return false;
    }

}
