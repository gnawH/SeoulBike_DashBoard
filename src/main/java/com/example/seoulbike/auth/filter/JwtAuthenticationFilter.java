package com.example.seoulbike.auth.filter;

import java.io.IOException;
import java.security.Key;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String secretKey = "secretKeysecretKeysecretKeysecretKey"; // 최소 32자
    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // 토큰 유효하지 않음
            }
        }

        filterChain.doFilter(request, response);
    }
}