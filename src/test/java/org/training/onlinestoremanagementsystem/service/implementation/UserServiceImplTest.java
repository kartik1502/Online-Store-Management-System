package org.training.onlinestoremanagementsystem.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UserDto;
import org.training.onlinestoremanagementsystem.entity.Role;
import org.training.onlinestoremanagementsystem.entity.User;
import org.training.onlinestoremanagementsystem.exception.NoSuchUserExists;
import org.training.onlinestoremanagementsystem.exception.PasswordDoseNotMatch;
import org.training.onlinestoremanagementsystem.exception.UserAlreadyExists;
import org.training.onlinestoremanagementsystem.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testLoadUserUsernameInvalidUser(){

        Mockito.when(userRepository.findUserByEmailId("kartikkulkarni1411@gmail.com")).thenReturn(Optional.empty());

        NoSuchUserExists exception = assertThrows(NoSuchUserExists.class,() -> userService.loadUserByUsername("kartikkulkarni1411@gmail.com"));
        assertEquals("User with email id kartikkulkarni1411@gmail.com does not exist",exception.getMessage());
    }

    @Test
    void testLoadUserUsernameValidUsername(){

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@1411");
        user.setRole(Role.USER);

        Mockito.when(userRepository.findUserByEmailId("kartikkulkarni1411@gmail.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("kartikkulkarni1411@gmail.com");
        assertNotNull(userDetails);
        assertEquals("kartikkulkarni1411@gmail.com",userDetails.getUsername());
        assertEquals("Ka3k@1411", userDetails.getPassword());
    }

    @Test
    void testRegisterUserAlreadyPresent() {

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@1411");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);

        Mockito.when(userRepository.findUserByEmailId("kartikkulkarni1411@gmail.com")).thenReturn(Optional.of(user));

        UserAlreadyExists exception = assertThrows(UserAlreadyExists.class, () -> userService.registerUser(userDto));
        assertEquals("User with email id kartikkulkarni1411@gmail.com already exists", exception.getMessage());
    }

    @Test
    void testRegisterPasswordDoseNotMatch() {

        UserDto userDto = new UserDto();
        userDto.setPassword("Ka3k@1411");
        userDto.setRepeatPassword("Ka3k@1311");

        Mockito.when(userRepository.findUserByEmailId(Mockito.anyString())).thenReturn(Optional.empty());

        PasswordDoseNotMatch exception = assertThrows(PasswordDoseNotMatch.class, () -> userService.registerUser(userDto));
        assertEquals("Password dose not match", exception.getMessage());
    }

    @Test
    void testRegisterUserRoleAdmin() {

        Mockito.when(userRepository.findUserByEmailId(Mockito.anyString())).thenReturn(Optional.empty());

        UserDto userDto = new UserDto();
        userDto.setPassword("Ka3k@1411");
        userDto.setRepeatPassword("Ka3k@1411");

        Mockito.when(userRepository.noOfUsers()).thenReturn(0);

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@1411");
        user.setFirstName("Karthik");
        user.setRole(Role.ADMIN);

        Mockito.when(userRepository.save(user)).thenReturn(user);

        ResponseDto responseDto = userService.registerUser(userDto);
        assertNotNull(responseDto);
        assertEquals("User Registered Successfully", responseDto.getResponseMessage());
    }

    @Test
    void testRegisterUserRoleUser() {

        Mockito.when(userRepository.findUserByEmailId(Mockito.anyString())).thenReturn(Optional.empty());

        UserDto userDto = new UserDto();
        userDto.setPassword("Ka3k@1411");
        userDto.setRepeatPassword("Ka3k@1411");

        Mockito.when(userRepository.noOfUsers()).thenReturn(2);

        User user = new User();
        user.setEmailId("kartikkulkarni1411@gmail.com");
        user.setPassword("Ka3k@1411");
        user.setFirstName("Karthik");
        user.setRole(Role.ADMIN);

        Mockito.when(userRepository.save(user)).thenReturn(user);

        ResponseDto responseDto = userService.registerUser(userDto);
        assertNotNull(responseDto);
        assertEquals("User Registered Successfully", responseDto.getResponseMessage());
    }


}
