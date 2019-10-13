package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(401);

        Map<String, Object> data = new HashMap<>();
        data.put("status", 401);
        data.put("massage", "Unauthorized");
        data.put("time", new Date());
        data.put("exception", e.getMessage());

        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(data));


    }
}
