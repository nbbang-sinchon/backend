package nbbang.com.nbbang.global.security;

import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider {
    //@Value("${secret.key}")
    private String secretKey = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY0NjEwMjQ2MywiaWF0IjoxNjQ2MTAyNDYzfQ._l4MpJZlb32_h-aSTyF77P8Kn-PL25Hio89YK_ntpMba_z9YfDi-aJdGDSYNnOYLC9okXyxB_YvYkmZLjqhrVQ";

    public String createToken(Authentication authentication) {

        //UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 360000);

        return Jwts.builder()
                .setSubject(Long.toString(2L))
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

}
