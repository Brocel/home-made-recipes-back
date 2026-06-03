package com.example.hmrback.exception.handler;

import com.example.hmrback.model.error.ApiError;
import com.example.hmrback.exception.util.ExceptionMessageEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.hmrback.exception.util.ExceptionUtils.toApiError;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError error = toApiError(HttpStatus.UNAUTHORIZED,
                ExceptionMessageEnum.AUTHENTICATION_REQUIRED,
                request.getRequestURI());


        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
