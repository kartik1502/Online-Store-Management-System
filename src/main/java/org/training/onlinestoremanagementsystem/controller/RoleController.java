package org.training.onlinestoremanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/admin")
public class RoleController {

    @Autowired
    private UserService userService;

    @PutMapping("/users")
    public ResponseEntity<ResponseDto> updateRole(@RequestHeader(value = "Authorization", required = false) String authToken, @Valid @RequestParam @Email(message = "Invalid email") String username,@Pattern(regexp = "(ADMIN)|(USER)|(EMPLOYEE)") @RequestParam String role){
        return new ResponseEntity<>(userService.updateRole(authToken, username, role), HttpStatus.OK);
    }
}
