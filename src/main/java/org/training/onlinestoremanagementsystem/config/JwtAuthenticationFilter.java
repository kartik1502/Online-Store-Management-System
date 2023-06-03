package org.training.onlinestoremanagementsystem.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.training.onlinestoremanagementsystem.dto.Constants.HEADER_STRING;
import static org.training.onlinestoremanagementsystem.dto.Constants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;


    Logger authLogger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HEADER_STRING);
        String jwtToken = null;
        String username = null;
        if((!Objects.isNull(header)) && header.startsWith(TOKEN_PREFIX)){

            jwtToken = header.replace(TOKEN_PREFIX, "");
            try{
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            }
            catch (IllegalArgumentException e){
                authLogger.error("Error occurred during getting username from token", e);
            }
            catch (ExpiredJwtException e){
                authLogger.error("Jwt Token Validation Failed", e);
            }
            catch (SignatureException e){
                authLogger.error("Authentication failed, invalid Creditials", e);
            }
        }
        else{
            authLogger.warn("Jwt Token does not begin with Bearer String");
        }

        if((!Objects.isNull(username)) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(jwtToken, userDetails))) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
