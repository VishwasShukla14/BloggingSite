package com.training.bloggingsite.services.impl;

import com.training.bloggingsite.dtos.RoleDto;
import com.training.bloggingsite.entities.Role;
import com.training.bloggingsite.entities.User;
import com.training.bloggingsite.exceptions.RoleAlreadyExistsException;
import com.training.bloggingsite.exceptions.RoleNotFoundException;
import com.training.bloggingsite.repositories.RoleRepository;
import com.training.bloggingsite.services.interfaces.RoleService;
import com.training.bloggingsite.utils.CriteriaQueryBuilder;
import com.training.bloggingsite.utils.RoleConvertor;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CriteriaQueryBuilder cb;

    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public RoleDto addRole(RoleDto roleDto) {

        List<Role> checkRoleInDB = cb.getResultWhereColumnEqual("name",roleDto.getName().toUpperCase(), Role.class);

        if (checkRoleInDB.isEmpty()) {
            Role roleToBeInserted = RoleConvertor.toRole(roleDto);
            roleToBeInserted.setName(roleToBeInserted.getName().toUpperCase());

            // Saved using JPA
            this.roleRepository.save(roleToBeInserted);

            logger.info("Role Added :" + roleDto);
            return roleDto;
        } else {
            throw new RoleAlreadyExistsException(roleDto.getName().toUpperCase());
        }
    }

    @Override
    public List<RoleDto> findAllRoles() {

        List<Role> roles = cb.getAll(Role.class);

        if (roles.isEmpty()) {
            throw new RoleNotFoundException();
        }

        List<RoleDto> roleDtos = roles.stream().map(RoleConvertor::toRoleDto).collect(Collectors.toList());
        logger.info("Roles Fetched :" + roleDtos);
        return roleDtos;
    }

    @Override
    @Transactional
    public void deleteRole(int id) {
        cb.deleteById("Id",id,Role.class);
        logger.info("Role Deleted with id :" + id);
    }

}
