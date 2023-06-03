package org.training.onlinestoremanagementsystem.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.AuthenticationRequest;
import org.training.onlinestoremanagementsystem.dto.AuthenticationResponse;
import org.training.onlinestoremanagementsystem.entity.User;
import org.training.onlinestoremanagementsystem.exception.NoSuchUserExists;
import org.training.onlinestoremanagementsystem.repository.UserRepository;
import org.training.onlinestoremanagementsystem.service.AuthenticationService;
import org.training.onlinestoremanagementsystem.service.UserService;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${spring.application.responseCode}")
    private String responseCode;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User loginUser = userRepository.findUserByEmailId(request.getUsername()).orElseThrow(
                () -> new NoSuchUserExists("User with email id " + request.getUsername() + " does not exist"));
        String jwtToken = jwtTokenUtil.generateToken(loginUser, userService.getAuthority(loginUser));
        return new AuthenticationResponse(responseCode, jwtToken, "Login Successful");
    }
}
