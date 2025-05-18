package com.project.readers_community.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Common JWT utility class for all microservices
 */
@Slf4j
@Component
public class JwtUtils {

   @Value("${app.jwt.secret}")
   private String jwtSecret;

   @Value("${app.jwt.expiration}")
   private int jwtExpirationMs;

   @Value("${app.jwt.refresh-expiration}")
   private int refreshExpirationMs;



   /**
    * Extract username from JWT token
    *
    * @param token the JWT token
    * @return username
    */
   public String getUsernameFromToken(String token) {
      return extractClaim(token, Claims::getSubject);
   }

   /**
    * Extract claim from token
    *
    * @param token          the JWT token
    * @param claimsResolver the claims resolver function
    * @return claim
    */
   public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
   }

   /**
    * Validate JWT token
    *
    * @param token the JWT token
    * @return true if valid
    */
   public boolean validateToken(String token) {
      try {
         Jwts.parserBuilder()
                 .setSigningKey(getSigningKey())
                 .build()
                 .parseClaimsJws(token);
         return true;
      } catch (Exception e) {
         log.error("JWT validation failed: {}", e.getMessage());
         return false;
      }
   }

   /**
    * Extract all claims from token
    *
    * @param token the JWT token
    * @return claims
    */
   private Claims extractAllClaims(String token) {
      return Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
   }

   /**
    * Get signing key
    *
    * @return signing key
    */
   private Key getSigningKey() {
      byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
      return Keys.hmacShaKeyFor(keyBytes);
   }

   /**
    * Check if token is expired
    *
    * @param token the JWT token
    * @return true if expired
    */
   public boolean isTokenExpired(String token) {
      final Date expiration = extractExpiration(token);
      return expiration.before(new Date());
   }

   /**
    * Extract expiration date from token
    *
    * @param token the JWT token
    * @return expiration date
    */
   public Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }

   /**
    * Generate JWT token for authenticated user
    *
    * @param authentication the authentication object
    * @return JWT token
    */
   public String generateJwtToken(Authentication authentication) {
      UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
      return generateTokenFromUsername(userPrincipal.getUsername());
   }

   public String generateJwtTokenWithRoles(String email, List<String> roles) {
//      UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
      return generateTokenWithRoles(email, roles);
   }

   /**
    * Generate JWT token from username
    *
    * @param username the username
    * @return JWT token
    */
   public String generateTokenFromUsername(String username) {
      Map<String, Object> claims = new HashMap<>();
      return Jwts.builder()
              .setClaims(claims)
              .setSubject(username)
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
              .signWith(getSigningKey(), SignatureAlgorithm.HS512)
              .compact();
   }
   public String generateTokenWithRoles(String username, List<String> roles) {
      Map<String, Object> claims = new HashMap<>();
      claims.put("roles", roles);

      return Jwts.builder()
              .setClaims(claims)
              .setSubject(username)
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
              .signWith(getSigningKey(), SignatureAlgorithm.HS512)
              .compact();
   }



   /**
    * Generate refresh token from username
    *
    * @param username the username
    * @return refresh token
    */
   public String generateRefreshTokenFromUsername(String username) {
      return Jwts.builder()
              .setSubject(username)
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + refreshExpirationMs))
              .signWith(getSigningKey(), SignatureAlgorithm.HS512)
              .compact();
   }

   /**
    * Validate JWT token
    *
    * @param authToken the JWT token
    * @return true if valid
    */
   public boolean validateJwtToken(String authToken) {
      try {
         Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
         return true;
      } catch (MalformedJwtException e) {
         log.error("Invalid JWT token: {}", e.getMessage());
      } catch (ExpiredJwtException e) {
         log.error("JWT token is expired: {}", e.getMessage());
      } catch (UnsupportedJwtException e) {
         log.error("JWT token is unsupported: {}", e.getMessage());
      } catch (IllegalArgumentException e) {
         log.error("JWT claims string is empty: {}", e.getMessage());
      }
      return false;
   }

   /**
    * Get username from JWT token
    *
    * @param token the JWT token
    * @return username
    */
   public String getUsernameFromJwtToken(String token) {
      return Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody()
              .getSubject();
   }
}
