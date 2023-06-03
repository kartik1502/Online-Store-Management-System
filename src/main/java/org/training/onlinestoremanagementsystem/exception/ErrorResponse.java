package org.training.onlinestoremanagementsystem.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String errorCode;

    private Set<String> errorMessages;
}
