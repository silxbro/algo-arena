package algo_arena.utils.jwt.service;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private final SignatureAlgorithm signatureAlgorithm = HS256;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-duration}")
    private Long accessDuration;

    private Key key;

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // JWT에서 만료 날짜 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰 유효성 검증
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 특정 클레임 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // 토큰 만료 여부 확인
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 사용자 이름으로 토큰 생성
    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), new HashMap<>());
    }

    // 토큰 생성
    private String createToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + accessDuration))
            .signWith(key, signatureAlgorithm)
            .compact();
    }
}