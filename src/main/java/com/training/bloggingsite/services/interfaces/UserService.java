package com.training.bloggingsite.services.interfaces;

import com.training.bloggingsite.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto findUserById(long id);

    UserDto findUserByEmail(String email);

    void updateUserRole(Long id, String role);
}
