package com.assessment.ecom.dto;

import com.assessment.ecom.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String roles;
    private String password;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.roles = user.getRoles();
    }
}
