package org.training.onlinestoremanagementsystem.service;

import org.training.onlinestoremanagementsystem.dto.AuthenticationRequest;
import org.training.onlinestoremanagementsystem.dto.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
