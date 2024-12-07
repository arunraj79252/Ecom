package com.assessment.ecom.service.ServiceImpl;

import com.assessment.ecom.dto.UserDTO;
import com.assessment.ecom.entity.User;
import com.assessment.ecom.exception.CustomException;
import com.assessment.ecom.repository.UserRepository;
import com.assessment.ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserDTO registerUser(UserDTO user) throws Exception {
        User userBeforeSave=new User(user);
            userBeforeSave.setRoles("ROLE_USER");
        userBeforeSave.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(userBeforeSave);
        LOGGER.info("User registered successfully!");
        return new UserDTO(savedUser);
    }
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException.BadRequestException("User not found!"));
        LOGGER.info("User fetched successfully");
        return new UserDTO(user);
    }
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToUserDTO).toList();
    }

    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(user);
    }
}
