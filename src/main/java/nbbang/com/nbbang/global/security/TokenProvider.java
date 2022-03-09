package nbbang.com.nbbang.global.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

import static nbbang.com.nbbang.global.security.SecurityPolicy.TOKEN_EXPIRE_TIME;
import static nbbang.com.nbbang.global.security.SecurityPolicy.TOKEN_SECRET_KEY;

@Service
@Slf4j
public class TokenProvider {
    private String secretKey = TOKEN_SECRET_KEY;
    public String createToken(Authentication authentication, Long memberId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(Long.toString(memberId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        log.info("Validating token...");
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
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
