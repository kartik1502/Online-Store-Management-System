package org.training.onlinestoremanagementsystem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.training.onlinestoremanagementsystem.entity.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static org.training.onlinestoremanagementsystem.dto.Constants.ACCESS_TOKEN_VALIDITY;
import static org.training.onlinestoremanagementsystem.dto.Constants.SIGNING_KEY;

@Component
public class JwtTokenUtil implements Serializable {

    private Claims getAllClaims(String token){

        return Jwts
                .parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver){
        Claims claims = getAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String generateToken(User user, List<SimpleGrantedAuthority> authorityList){
        return generateToken(user.getEmailId(), authorityList);
    }

    private String generateToken(String subject, List<SimpleGrantedAuthority> authorityList){

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", authorityList);

        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();
    }

    private Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
