package com.swarga.Kartwala.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
   This class implements the AuthenticationEntryPoint interface to handle unauthorized access attempts.
*/
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        logger.error("Unauthorized error: {}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String,Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), body);
        // This will write the response body as JSON with the error details
        // The response will include the status, error, message, and path of the request
        // This is useful for debugging and providing feedback to the client about the unauthorized access attempt.
        // The client will receive a JSON response with the error details, which can be used to
        // inform the user or log the error for further analysis.
        // This is particularly useful in RESTful APIs where JSON is the preferred format for communication.
        // The response will be sent back to the client with a 401 Unauthorized status code,
        // indicating that the request requires authentication or the provided credentials are invalid.

    }
}
