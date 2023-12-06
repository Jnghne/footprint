package project.footprint.api.user.oauth.dto;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Getter
@Component
public class JwtInfo {

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    @Value("${jwt.secret-key}")
    private String secretKey;

    public Key getKey(){
        byte[] secretKeyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyByte);
    }

}
