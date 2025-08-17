package com.swarga.Kartwala.security.jwt;

import com.swarga.Kartwala.security.services.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationMs}")
    private long jwtExpirationTimeMs;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;

    //Getting the JWT from the request header
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.info("Bearer token: {}", bearerToken);
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }else {
            logger.error("Token not passed properly!!");
            throw new IllegalArgumentException("Token not passed properly!!");
        }
    }

    //Using cookie based Authentication
    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie); //request, name of the cookie
        if(cookie!=null){
            return cookie.getValue();
        }else{
            return null;
        }

    }

    //Get Clean cookie. This will signout the user
    public ResponseCookie getCleanCookieJwt(){
        ResponseCookie responseCookie = ResponseCookie.from(jwtCookie,null)
                .path("/api") //Restricts the cookie to be sent only with requests to the /api path.
                .build();
        return responseCookie;
    }

    //Method to generate JwtCookie
    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails){
       String jwt = generateTokenFromUsername(userDetails);
       ResponseCookie responseCookie = ResponseCookie.from(jwtCookie,jwt)
               .path("/api") //Restricts the cookie to be sent only with requests to the /api path.
               .maxAge(24*60*60)
               .httpOnly(false) //Indicates that the cookie is not restricted to HTTP requests only (it can be accessed via JavaScript).
               .build();
       return responseCookie;
    }

    //Generating token from Username
    public String generateTokenFromUsername(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        logger.info("Generating token for username: {}", username);
        return Jwts.builder().subject(username).issuedAt(new Date())
                .expiration(new Date( new Date().getTime() + jwtExpirationTimeMs))
                .signWith(key()).compact();
    }

    //Getting Username from JWT token
    public String getUserNameFromJwtToken(String authToken) {
        logger.info("Getting username from JWT token: {}", authToken);
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken).getPayload().getSubject();
    }

    //Generating signing key
    public Key key() {
        logger.info("Generating signing key");
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
    //Validate JWT token
    public boolean validateToken(String authToken) {
        logger.info("Validating token: {}", authToken);
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while validating JWT token: {}", e.getMessage());
        }
        return false;
    }

}
