package org.training.onlinestoremanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Pattern(regexp = "[a-zA-Z ]+", message = "First Name should be alphabets only")
    private String firstName;

    @NotNull
    @Pattern(regexp = "[a-zA-Z ]+", message = "Last Name should be alphabets only")
    private String lastName;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}", message = "Email should be valid")
    private String emailId;

    @NotNull
    @Min(18)
    @Max(62)
    private int age;

    @NotNull
    @Pattern(regexp = "[6-9][0-9]{9}", message = "Contact Number should be valid")
    private String contactNumber;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Password should be valid")
    private String password;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Repeat Password should be valid")
    private String repeatPassword;

}
