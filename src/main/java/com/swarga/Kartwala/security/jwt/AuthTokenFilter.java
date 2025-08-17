package com.swarga.Kartwala.security.jwt;

import com.swarga.Kartwala.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
    This class extends OncePerRequestFilter to ensure that the filter is executed once per request.
    It is responsible for extracting the JWT from the request header, validating it, and setting the authentication in the security context.

    Flow if JWT is valid:
    Request → AuthTokenFilter → JWT validated → auth set → CONTROLLER

    Fallback flow if JWT is invalid:
    Request → AuthTokenFilter → no JWT → UsernamePasswordAuthenticationFilter → login attempt

 */
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try{
            // Step 1: Get the JWT from the request header
            String jwt = parseJwt(request);
            logger.info("Generated AuthToken: {}", jwt);
            //Step2: Check if the JWT is not null and validate it
            if(jwt!=null && jwtUtils.validateToken(jwt)){
                //If the JWT ius valid, get the username from the JWT
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                //Step3: Load the user details using the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //Step4: Create an authentication token
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                //Step5: Set the details of the authentication token
                authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                //Step6: Set the authentication token in the security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }catch (Exception e){
            logger.error("Cannot authenticate user: {}", e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
    // In AuthTokenFilter.java
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String path = request.getRequestURI();
//        return path.startsWith("/h2-console") || path.equals("/favicon.ico");
//    }
    private String parseJwt(HttpServletRequest request) {
        return jwtUtils.getJwtFromCookies(request);
    }
}
