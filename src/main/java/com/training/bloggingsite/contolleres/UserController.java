package com.training.bloggingsite.contolleres;

import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.services.interfaces.UserService;
import com.training.bloggingsite.utils.FileFinder;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Value("${uploadDir}")
    private String UPLOAD_DIR;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    // Logging Page
    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    // Redirecting the USER to User-Dashboard.
    @GetMapping("/user/home")
    public String getUser(Principal principal, Model model) {

        UserDto userDto = this.userService.findUserByEmail(principal.getName());
        model.addAttribute("name", userDto.getName());

        String[] name = userDto.getEmail().split("@");
        String profileImageName = name[0]+".jpg";

        if(FileFinder.checkProfileInImg(profileImageName)) model.addAttribute("profile", "/img/"+profileImageName);

        else model.addAttribute("profile", "/img/"+"default.jpg");

        return "user/dashboard";
    }

    // Register User, default role will be USER.
    @GetMapping("/register")
    public ModelAndView getRegisterUser() {
        ModelAndView mav = new ModelAndView("signup");
        mav.addObject("userData", new UserDto());
        return mav;
    }

    // Saving the User to database.
    @PostMapping("/register/save")
    public String postRegisterUser(@Valid @ModelAttribute("userData") UserDto userDto, BindingResult result, @RequestParam("file") MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            logger.error(String.valueOf(result));
            return "redirect:/register";
        }
        if (!file.isEmpty()) {
            String[] name = userDto.getEmail().split("@");
            file.transferTo(new File(UPLOAD_DIR + name[0]+".jpg"));
        }
        this.userService.addUser(userDto);
        return "redirect:/login";
    }

}



