package com.training.bloggingsite.contolleres;

import com.training.bloggingsite.dtos.RoleDto;
import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.services.interfaces.RoleService;
import com.training.bloggingsite.services.interfaces.UserService;
import com.training.bloggingsite.utils.FileFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    // Redirecting the ADMIN to Admin-Dashboard.
    @GetMapping("/admin/home")
    public String getAdmin(Principal principal, Model model){
        UserDto userDto = this.userService.findUserByEmail(principal.getName());
        model.addAttribute("name",userDto.getName());

        String[] name = userDto.getEmail().split("@");
        String profileImageName = name[0]+".jpg";

        if(FileFinder.checkProfileInImg(profileImageName))
            model.addAttribute("profile", "/img/"+profileImageName);

        else
            model.addAttribute("profile", "/img/"+"default.jpg");

        return "admin/dashboard";
    }

    // Displaying all the Users.
    @GetMapping("/admin/view-users")
    public ModelAndView viewUserList(Principal principal){
        List<UserDto> users = this.userService.findAllUsers();
        ModelAndView mav = new ModelAndView("admin/users");

        UserDto userDto = this.userService.findUserByEmail(principal.getName());
        mav.addObject("name", userDto.getName());

        String[] name = principal.getName().split("@");
        String profileImageName = name[0] + ".jpg";

        if (FileFinder.checkProfileInImg(profileImageName)) mav.addObject("profile", "/img/" + profileImageName);
        else mav.addObject("profile", "/img/" + "default.jpg");


        mav.addObject("users",users);
        return mav;
    }

    // Changing the role of the Present User.
    @GetMapping("/admin/user/update-role")
    public String updateUserRole(@RequestParam("id") long id,@RequestParam("role") String role){
        this.userService.updateUserRole(id,role);
        return "redirect:/admin/view-users";
    }

}
