package com.training.bloggingsite;


import com.training.bloggingsite.contolleres.UserController;
import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.repositories.RoleRepository;
import com.training.bloggingsite.repositories.UserRepository;
import com.training.bloggingsite.services.interfaces.RoleService;
import com.training.bloggingsite.services.interfaces.UserService;
import com.training.bloggingsite.utils.CriteriaQueryBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.security.Principal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

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
    public void testSaveUser() throws Exception {
        when(userService.addUser(any(UserDto.class))).thenReturn(any(UserDto.class));
        this.mockMvc.perform(post("/register/save")).andExpect(status().is(302));
    }

    @Test
    public void testFindUserByEmail() throws Exception{
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(userService.findUserByEmail(mockPrincipal.getName())).thenReturn(new UserDto());
        this.mockMvc.perform(get("/user/home").principal(mockPrincipal)).andExpect(status().isOk());
    }


}
