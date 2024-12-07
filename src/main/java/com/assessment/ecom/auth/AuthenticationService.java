package com.assessment.ecom.auth;

import com.assessment.ecom.dto.AuthenticationRequestDTO;
import com.assessment.ecom.dto.AuthenticationResponseDTO;
import com.assessment.ecom.dto.RefreshTokenRequestDTO;
import com.assessment.ecom.exception.CustomException;
import com.assessment.ecom.service.ServiceImpl.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) throws Exception {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            authenticationManager.authenticate(authenticationToken);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority) // Get the role string from GrantedAuthority
                    .toList();

            final String jwt = jwtUtil.generateAccessToken(userDetails.getUsername(), roles);

            final String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            return new AuthenticationResponseDTO(jwt, refreshToken);
        } catch (Exception e) {
            LOGGER.error("Error when generating token" + e);
            throw new CustomException.BadRequestException("Incorrect username or password");
        }
    }

    public AuthenticationResponseDTO refreshToken(RefreshTokenRequestDTO request) throws Exception {
        String refreshToken = request.getToken();
        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority) // Get the role string from GrantedAuthority
                    .toList();
            final String newAccessToken = jwtUtil.generateAccessToken(username, roles);
            return new AuthenticationResponseDTO(newAccessToken, refreshToken);
        } else {
            throw new CustomException.BadRequestException("Invalid refresh token");
        }
    }
}
