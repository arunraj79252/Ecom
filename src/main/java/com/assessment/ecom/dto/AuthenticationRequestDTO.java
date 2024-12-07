package com.assessment.ecom.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class AuthenticationRequestDTO {
    private String username;
    private String password;
}
