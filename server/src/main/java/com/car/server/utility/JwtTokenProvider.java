package com.car.server.utility;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secrect}") // config at application.properties
    private String jwtSecret;

    @Value("${jwt.expiration}") // Token 有效期(毫秒)
    private long jwtExpiration;

    //生成Token
    public String generateToken(String userId){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
//                .setIssuedAt(new Date())  //令牌生成时间 测试方便注释
//                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration)) //令牌过期时间 测试方便注释
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    //从JWT Token 中解析出Id
    public String getUserIdFromToken(String token){
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            return claimsJws.getBody().getSubject(); //提取用户Id
        }catch (ExpiredJwtException  ex){
            // Token 无效 或 过期
            throw new JwtException("Token expired at " + ex.getClaims().getExpiration());
        }catch (MalformedJwtException ex){
            throw new JwtException("Invalid token format");
        }catch (JwtException | IllegalArgumentException ex) {
            throw new JwtException("Token validation failed: " + ex.getMessage());
        }
    }





}
