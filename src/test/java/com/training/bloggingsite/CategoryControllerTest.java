package com.training.bloggingsite;

import com.training.bloggingsite.contolleres.CategoryController;
import com.training.bloggingsite.entities.Category;
import com.training.bloggingsite.repositories.CategoryRepository;
import com.training.bloggingsite.repositories.RoleRepository;
import com.training.bloggingsite.repositories.UserRepository;
import com.training.bloggingsite.services.interfaces.CategoryService;
import com.training.bloggingsite.utils.CriteriaQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private  UserRepository userRepository;

    @MockBean
    CriteriaQueryBuilder cb;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void testGetCategory() throws Exception {
        Category category = new Category();
        category.setId(1);
        category.setName("Parent");
        List<Category> categories = List.of(category);
        when(cb.getAll(Category.class)).thenReturn(categories);

        this.mockMvc.perform(get("/admin/view-categories")).andExpect(status().isOk());
    }

}
