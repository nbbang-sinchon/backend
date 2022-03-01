package nbbang.com.nbbang.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private String secretKey = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY0NjEwMjQ2MywiaWF0IjoxNjQ2MTAyNDYzfQ._l4MpJZlb32_h-aSTyF77P8Kn-PL25Hio89YK_ntpMba_z9YfDi-aJdGDSYNnOYLC9okXyxB_YvYkmZLjqhrVQ";
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    private Long tokenExpired = 360000L;

    //private Boolean isTokenExpired(String token) {
    //    return extractExpiration(token).isBefore(LocalDateTime.now());
    //}

    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username, tokenExpired);
    }

    public String generateToken(Map<String, Object> claims, String subject, Long expiryTime) {
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(expiryTime);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(toDate(LocalDateTime.now()))
                .setExpiration(toDate(expiryDate))
                .signWith(signatureAlgorithm, secretKey)
                .compact();
    }



}
