package com.training.bloggingsite.exceptions;

import com.training.bloggingsite.dtos.CategoryDto;
import com.training.bloggingsite.dtos.RoleDto;
import com.training.bloggingsite.dtos.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandling {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandling.class);

    @ExceptionHandler(value = RoleAlreadyExistsException.class)
    public String roleAlreadyExistsExceptionHandler(Model model, RoleAlreadyExistsException e){
        model.addAttribute("error",e.getMessage());
        model.addAttribute("roleData",new RoleDto());
        logger.error(e.getMessage());
        return "redirect:/admin/view-role";
    }

    @ExceptionHandler(value = UserEmailAlreadyExistsException.class)
    public String userEmailAlreadyExistsExceptionHandler(Model model,UserEmailAlreadyExistsException e){
        model.addAttribute("error",e.getMessage());
        model.addAttribute("userData",new UserDto());
        logger.error(e.getMessage());
        return "register-user";
    }
    @ExceptionHandler(value = CategoryAlreadyExistsException.class)
    public String CategoryAlreadyExistsExceptionHandler(Model model,CategoryAlreadyExistsException e){
        model.addAttribute("error",e.getMessage());
        model.addAttribute("categoryData",new CategoryDto());
        logger.error(e.getMessage());
        return "add-category";
    }
    @ExceptionHandler(value = SubCategoryAlreadyExistsException.class)
    public String subCategoryAlreadyExistsExceptionHandler(Model model,SubCategoryAlreadyExistsException e){
        model.addAttribute("error",e.getMessage());
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("categoryData",categoryDto);
        model.addAttribute("parentId",e.getParentId());
        logger.error(e.getMessage());
        return "add-sub-category";
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public void roleNotFoundExceptionHandler(){
        logger.error("Role not found");
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public void SQLIntegrityConstraintViolationExceptionHandling(SQLIntegrityConstraintViolationException e){
        logger.error("Cannot Delete because "+e.getMessage());
    }

}
