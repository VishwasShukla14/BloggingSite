package com.training.bloggingsite;


import com.training.bloggingsite.contolleres.AdminController;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    CriteriaQueryBuilder cb;

    @Test
    @WithMockUser(username = "admin@admin.com", password = "Mind@123", roles = {"ADMIN"})
    public void testFindUserById() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        String id = "1";
        when(userService.findUserById(Long.parseLong(id))).thenReturn(new UserDto());
        this.mockMvc.perform(get("/admin/user/view").param("id", id).principal(mockPrincipal)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", password = "Mind@123", roles = {"ADMIN"})
    public void testFindAllUser() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        when(userService.findAllUsers()).thenReturn(new ArrayList<UserDto>());
        this.mockMvc.perform(get("/admin/user/view-users").principal(mockPrincipal)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testUpdateRole() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        String id = "1";
        String role = "ADMIN";
        doNothing().when(userService).updateUserRole(Long.parseLong(id),role);
        this.mockMvc.perform(get("/admin/user/update-role").principal(mockPrincipal).param("id",id).param("role",role)).andExpect(status().is(302));

    }

}
