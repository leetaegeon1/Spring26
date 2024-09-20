package com.sparta.spring26.util;

import com.sparta.spring26.domain.user.entity.UserRole;
import com.sparta.spring26.enums.BaseResponseEnum;
import com.sparta.spring26.exception.JwtTokenExceptionHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
//@Configuration
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORIZATION_KEY = "Auth";

    public static final String BEARER_PREFIX = "Bearer ";

    private final long TOKEN_TIME = 60 * 60 * 1000;

    @Value("${jwt.secret_key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = Logger.getLogger("JWT");

    @PostConstruct
    public void init() {
        System.out.println("SecretKey: " + secretKey);
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String email, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_HEADER, role)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key)
                .compact();
    }

    public void addJwtToHeader(String token, HttpServletResponse res) {
        res.addHeader(AUTHORIZATION_HEADER, token);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.severe("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.");
            throw new JwtTokenExceptionHandler(BaseResponseEnum.JWT_NOT_VALID);
        } catch (ExpiredJwtException e) {
            logger.severe("Expired JWT token, 만료된 JWT token 입니다");
            throw new JwtTokenExceptionHandler(BaseResponseEnum.JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            logger.severe("Unsupported JWT token, 지원되지 않은 JWT 토큰 입니다");
            throw new JwtTokenExceptionHandler(BaseResponseEnum.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            logger.severe("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new JwtTokenExceptionHandler(BaseResponseEnum.JWT_NOT_FOUND);
        }
        return true;
    }

    public Collection<? extends GrantedAuthority> getUserAuthorityFromToken(String token) {
        return List.of(new SimpleGrantedAuthority((String) getUserInfoFromToken(token).get(AUTHORIZATION_KEY)));
    }
}
