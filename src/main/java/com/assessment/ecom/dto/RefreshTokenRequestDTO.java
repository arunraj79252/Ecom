package com.assessment.ecom.dto;

import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    private String token;

    public RefreshTokenRequestDTO(String token) {
        this.token = token;
    }
}
