package com.training.bloggingsite;

import com.training.bloggingsite.contolleres.RoleController;
import com.training.bloggingsite.dtos.RoleDto;
import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.repositories.RoleRepository;
import com.training.bloggingsite.repositories.UserRepository;
import com.training.bloggingsite.services.interfaces.RoleService;
import com.training.bloggingsite.services.interfaces.UserService;
import com.training.bloggingsite.utils.CriteriaQueryBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RoleService roleService;

    @MockBean
    CriteriaQueryBuilder cb;

    @Test
    @WithMockUser(username = "admin@admin.com", password = "Mind@123", roles = {"ADMIN"})
    public void testSaveRole() throws Exception {
        when(roleService.addRole(any(RoleDto.class))).thenReturn(new RoleDto());
        this.mockMvc.perform(post("/admin/add-role-save")).andExpect(status().is(302));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", password = "Mind@123", roles = {"ADMIN"})
    public void testFindAllRole() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        when(roleService.findAllRoles()).thenReturn(new ArrayList<RoleDto>());
        this.mockMvc.perform(get("/admin/view-role").principal(mockPrincipal)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testDeleteRole() throws Exception{
        Principal mockPrincipal = Mockito.mock(Principal.class);
        doNothing().when(roleService).deleteRole(1);
        this.mockMvc.perform(get("/admin/delete-role").principal(mockPrincipal).param("id","1")).andExpect(status().is(302));
    }


}
