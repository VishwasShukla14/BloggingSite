package com.training.bloggingsite.services.interfaces;

import com.training.bloggingsite.dtos.RoleDto;

import java.util.List;

public interface RoleService {

    public RoleDto addRole(RoleDto roleDto);

    public List<RoleDto> getAllRoles();

    public void deleteRole(int id);

    public RoleDto getRoleById(int id);

    public RoleDto getRole(String role);

}
