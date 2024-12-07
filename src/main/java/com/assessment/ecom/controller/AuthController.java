package com.assessment.ecom.controller;

import com.assessment.ecom.auth.AuthenticationService;
import com.assessment.ecom.dto.AuthenticationRequestDTO;
import com.assessment.ecom.dto.AuthenticationResponseDTO;
import com.assessment.ecom.dto.RefreshTokenRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO request) throws Exception {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request) throws Exception {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}
