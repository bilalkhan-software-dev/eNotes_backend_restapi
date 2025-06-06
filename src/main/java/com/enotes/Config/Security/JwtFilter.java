package com.enotes.Config.Security;

import com.enotes.Handler.GenericResponse;
import com.enotes.Service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtFilter doFilterInternal() Start");
        try {

            String authHeader = request.getHeader("Authorization");
            // Bearer token = Bearer *************.*********.*************

            String token = null;
            String username = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")){
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    boolean isTokenValid = jwtService.validateToken(token, userDetails);
                    if (isTokenValid){
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }

            }
        }catch (Exception e){
            generateResponse(response,e);
            log.error("JwtFilter line # 60 error :{}",e.getMessage());
            return;
        }
        filterChain.doFilter(request,response);
        log.info("JwtFilter doFilterInternal() End");
    }

    private void generateResponse(HttpServletResponse response,Exception e) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Object errors = GenericResponse.builder()
                .status("Failed!")
                .message(e.getMessage())
                .responseHttpStatus(HttpStatus.UNAUTHORIZED)
                .build().create().getBody();
        response.getWriter().write(new ObjectMapper().writeValueAsString(errors));
    }

}
