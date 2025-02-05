package com.api.security.jwt;

import ch.qos.logback.core.util.StringUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);

            if(token == null){
                filterChain.doFilter(request,response);
                return;
            }

            String username = null;

            username = jwtService.extractUsername(token);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                if(jwtService.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null,userDetails.getAuthorities()
                    );

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

                filterChain.doFilter(request,response);
            }
        } catch (ServletException | IOException ex){
            setErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response, ex.getMessage());
        } catch (JwtException ex) {
            setErrorResponse(HttpServletResponse.SC_BAD_REQUEST, response, ex.getMessage());
        }

    }

    private void setErrorResponse(int status, HttpServletResponse response, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{"
                + "\"flag\": false,"
                + "\"code\":" + status + ","
                + "\"message\":\"" + message + "\","
                + "\"data\": null"
                + "}");
    }

    private String getToken(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) return token.substring(7);
        return null;
    }
}
