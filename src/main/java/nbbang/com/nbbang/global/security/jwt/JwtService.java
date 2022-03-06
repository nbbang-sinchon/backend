
package nbbang.com.nbbang.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "lkajsdlkjasdsadasd";
    private static final Long expireTime = 1000L * 30;

    public String createTokenForSocket(Long id) {
/*        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyByte = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(secretKeyByte, signatureAlgorithm.getJcaName());

        String subject = String.valueOf(id);
        return Jwts.builder()
                .setSubject(subject)
                .signWith(signingKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis()+expireTime))
                .compact();*/
        return "hello";
    }

    public Long validateByToken(String token) {
        return 1L;
    }
}

/*        // *************** 토큰 예외 던지는 부분 추가 ****************** //
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
        String subject = claims.getSubject();
        Long id = Long.valueOf(subject);*//*

        Long id = Long.valueOf(token);
        return id;
    }

}
*/
