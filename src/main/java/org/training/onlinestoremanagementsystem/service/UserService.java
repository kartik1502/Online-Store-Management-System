package org.training.onlinestoremanagementsystem.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UserDto;
import org.training.onlinestoremanagementsystem.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    ResponseDto registerUser(UserDto userDto);

    List<SimpleGrantedAuthority> getAuthority(User user);
}
