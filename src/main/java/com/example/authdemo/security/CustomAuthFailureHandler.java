package com.example.authdemo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger log =
            LoggerFactory.getLogger(CustomAuthFailureHandler.class);

    private final String failureRedirectUrl;

    public CustomAuthFailureHandler(String failureRedirectUrl) {
        this.failureRedirectUrl = failureRedirectUrl;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {


        log.warn("Authentication failed for username: {}",
                request.getParameter("username"));


        request.getSession().setAttribute(
                "LOGIN_ERROR_MESSAGE",
                "Authentication failed: Invalid username or password"
        );


        request.getSession().setAttribute(
                "LOGIN_ATTEMPTED_USERNAME",
                request.getParameter("username")
        );


        response.sendRedirect(
                request.getContextPath() + failureRedirectUrl
        );
    }
}