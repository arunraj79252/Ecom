package com.assessment.ecom.service;


import com.assessment.ecom.dto.UserDTO;

import java.util.List;

public interface UserService {
    public UserDTO registerUser(UserDTO user) throws Exception;

    public UserDTO getUserById(Long id);

    public List<UserDTO> getAllUsers();

}