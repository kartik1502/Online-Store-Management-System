package org.training.onlinestoremanagementsystem.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.config.JwtTokenUtil;
import org.training.onlinestoremanagementsystem.dto.AuthenticationRequest;
import org.training.onlinestoremanagementsystem.dto.AuthenticationResponse;
import org.training.onlinestoremanagementsystem.exception.NoSuchUserExists;
import org.training.onlinestoremanagementsystem.repository.UserRepository;
import org.training.onlinestoremanagementsystem.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void testAuthenticateInvalidUserDetails() {

        String username = "karthikkulkarni1411@gmail.com";
        String password = "Ka3k@1411";
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(userService.loadUserByUsername(username)).thenReturn(userDetails);

        assertThrows(NoSuchUserExists.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testAuthenticateInvalidUser() {

        UserDetails userDetails = new User("karthikkulkarni1411@gmail.com", "Ka3k@1411", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Mockito.when(userService.loadUserByUsername("karthikkulkarni1411@gmail.com")).thenReturn(userDetails);

        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("karthikkulkarni1411@gmail.com", "Ka3k@1411"))).thenReturn(null);
        Mockito.when(userRepository.findUserByEmailId("karthikkulkarni1411@gmail.com")).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExists.class, () -> authenticationService.authenticate(new AuthenticationRequest("karthikkulkarni1411@gmail.com", "Ka3k@1411")));
    }

    @Test
    void testAuthenticateValidUser() {

        UserDetails userDetails = new User("karthikkulkarni1411@gmail.com", "Ka3k@1411", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Mockito.when(userService.loadUserByUsername("karthikkulkarni1411@gmail.com")).thenReturn(userDetails);

        org.training.onlinestoremanagementsystem.entity.User user = new org.training.onlinestoremanagementsystem.entity.User();
        user.setEmailId("karthikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@1411");

        Mockito.when(userRepository.findUserByEmailId("karthikkulkarni1411@gmail.com")).thenReturn(Optional.of(user));

        String jwtToken = "dhgfasdfqwtr75894ruqwetrhjbfsuidetqytrqwe4riweojkrhjfgsdlkag";
        Mockito.when(jwtTokenUtil.generateToken(user, userService.getAuthority(user))).thenReturn(jwtToken);

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(new AuthenticationRequest("karthikkulkarni1411@gmail.com", "Ka3k@1411"));
        assertEquals(jwtToken, authenticationResponse.getJwtToken());
    }

}
