package com.training.bloggingsite.contolleres;

import com.training.bloggingsite.dtos.RoleDto;
import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.services.interfaces.RoleService;
import com.training.bloggingsite.services.interfaces.UserService;
import com.training.bloggingsite.utils.FileFinder;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    // Save new Role to database, also contains the model for adding new Role
    @PostMapping("admin/add-role-save")
    public String postAddRole(@Valid @ModelAttribute("roleData") RoleDto roleDto, BindingResult result) {
        if (result.hasErrors()) {
            logger.error(String.valueOf(result));
            return "redirect:/admin/view-role";
        }
        this.roleService.addRole(roleDto);
        return "redirect:/admin/view-role";
    }

    // Display role Screen on Dashboard.
    @GetMapping("admin/view-role")
    public ModelAndView viewRoleList(Principal principal) {
        List<RoleDto> roles = this.roleService.findAllRoles();

        ModelAndView mav = new ModelAndView("admin/role");
        mav.addObject("roles", roles);

        mav.addObject("roleData", new RoleDto());

        UserDto userDto = this.userService.findUserByEmail(principal.getName());
        mav.addObject("name", userDto.getName());

        String[] name = principal.getName().split("@");
        String profileImageName = name[0] + ".jpg";

        if (FileFinder.checkProfileInImg(profileImageName)) mav.addObject("profile", "/img/" + profileImageName);
        else mav.addObject("profile", "/img/" + "default.jpg");

        return mav;
    }

    // Delete the selected role.
    @GetMapping("admin/delete-role")
    public String deleteRole(@RequestParam("id") int id) {
        this.roleService.deleteRole(id);
        return "redirect:/admin/view-role";
    }

}
