package com.enotes.Service.Impl;

import com.enotes.Entity.User;
import com.enotes.Exception.JwtTokenExpiredException;
import com.enotes.Service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private String secretKey="";

    public JwtServiceImpl() {
        log.info("JwtServiceImpl Constructor() start");
        try {

            KeyGenerator keyGeneratedInstance = KeyGenerator.getInstance("HmacSHA256");
            SecretKey generatedKey = keyGeneratedInstance.generateKey();
            secretKey = Base64.getEncoder().encodeToString(generatedKey.getEncoded());
            log.info("JwtServiceImpl Constructor() end");
        } catch (Exception e) {
            log.error("JwtServiceImpl Constructor caught exception :{}", e.getMessage());
        }
    }

    @Override
    public String generateToken(User user) {
        log.info("JwtServiceImpl generateToken(User user) Start");

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRoles());
        claims.put("status", user.getStatus().getIsEnabled());
        String token = Jwts.builder()
                .claims().add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 10))
                .and()
                .signWith(getKey())
                .compact();
        log.info("JwtServiceImpl generateToken(User user) before return");
        return token;
    }

    private Key getKey() {
        log.info("JwtServiceImpl getKey() Start");
        byte[] decode = Decoders.BASE64.decode(secretKey);
        log.info("JwtServiceImpl getKey() End");
        return Keys.hmacShaKeyFor(decode);
    }

    private SecretKey decryptKey(String token) {
        log.info("JwtServiceImpl decryptKey() Start");
        byte[] decodedKeys = Decoders.BASE64.decode(secretKey);
        log.info("JwtServiceImpl decryptKey() End before return");
        return Keys.hmacShaKeyFor(decodedKeys);
    }

    private Claims extractAllClaims(String token){
        try {
            return Jwts.parser()
                    .verifyWith(decryptKey(token))
                    .build().parseSignedClaims(token)
                    .getPayload();
        }catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("Token is expired!");
        }
        catch (JwtException e){
            throw new JwtTokenExpiredException("Jwt token is invalid!");
        }catch (Exception e){
            log.error("Error in extracting claims {}",e.getMessage());
            throw e;
        }
    }



    @Override
    public String extractUsername(String token) {
        log.info("JwtServiceImpl extractUsername() Start");
        Claims allClaims = extractAllClaims(token);
        log.info("JwtServiceImpl extractUsername()  just before return");
        return allClaims.getSubject();
    }

    private String extractRole(String token){
        log.info("JwtServiceImpl extractRole() Start");
        Claims allClaims = extractAllClaims(token);
        String role = (String) allClaims.get("role");
        log.info("JwtServiceImpl extractRole()  just before return");
        return role;
    }

    private Boolean isTokenExpired(String token){
        log.info("JwtServiceImpl isTokenExpired() Start");
        Claims allClaims = extractAllClaims(token);
        Date expiryDate = allClaims.getExpiration();
        log.info("JwtServiceImpl isTokenExpired()  just before return");
        return expiryDate.before(new Date());
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        log.info("JwtServiceImpl validateToken() Start");

        String username = extractUsername(token);
        Boolean isTokenExpired = isTokenExpired(token);
        if (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired){
            return true;
        }
        log.info("JwtServiceImpl validateToken()  just before return");
        return false;
    }

}
