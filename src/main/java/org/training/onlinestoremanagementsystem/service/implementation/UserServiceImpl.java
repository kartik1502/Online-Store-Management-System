package org.training.onlinestoremanagementsystem.service.implementation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UserDto;
import org.training.onlinestoremanagementsystem.entity.Role;
import org.training.onlinestoremanagementsystem.entity.User;
import org.training.onlinestoremanagementsystem.exception.NoSuchUserExists;
import org.training.onlinestoremanagementsystem.exception.PasswordDoseNotMatch;
import org.training.onlinestoremanagementsystem.exception.UserAlreadyExists;
import org.training.onlinestoremanagementsystem.repository.UserRepository;
import org.training.onlinestoremanagementsystem.service.UserService;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Value("${spring.application.responseCode}")
    private String responseCode;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findUserByEmailId(username);
        if(!user.isPresent()){
            throw new NoSuchUserExists("User with email id " + username + " does not exist");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmailId(), user.get().getPassword(), getAuthority(user.get()));
    }

    public List<SimpleGrantedAuthority> getAuthority(User user){
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public ResponseDto registerUser(UserDto userDto) {

        Optional<User> user = userRepository.findUserByEmailId(userDto.getEmailId());
        if(user.isPresent()){
            throw new UserAlreadyExists("User with email id " + userDto.getEmailId() + " already exists");
        }
        if(!userDto.getPassword().equals(userDto.getRepeatPassword())) {
            throw new PasswordDoseNotMatch("Password dose not match");
        }
        User registerUser = new User();
        if(userRepository.noOfUsers() < 1){
            registerUser.setRole(Role.ADMIN);
        }
        else{
            registerUser.setRole(Role.USER);
        }
        BeanUtils.copyProperties(userDto, registerUser, "repeatPassword");
        registerUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(registerUser);
        return new ResponseDto(responseCode, "User Registered Successfully");
    }
}
