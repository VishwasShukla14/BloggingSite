package com.training.bloggingsite.contolleres;

import com.training.bloggingsite.dtos.CategoryDto;
import com.training.bloggingsite.dtos.PostDto;
import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.services.interfaces.CategoryService;
import com.training.bloggingsite.entities.Post;
import com.training.bloggingsite.services.interfaces.BookmarkService;
import com.training.bloggingsite.services.interfaces.PostService;
import com.training.bloggingsite.services.interfaces.UserService;
import com.training.bloggingsite.utils.UserConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

//comment
@Controller
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;


    Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("admin/delete-post")
    public String deletePost(@RequestParam("id") long id) {
        this.postService.deletePost(id);
        return "redirect:/admin/all-post";
    }

    @GetMapping("admin/all-post")
    public ModelAndView getAllPostForAdmin() {
        List<PostDto> postDto = this.postService.getAllPost();
        ModelAndView mav = new ModelAndView("admin-view-all-post");
        mav.addObject("postData", postDto);
        return mav;
    }

    @GetMapping("user/all-post")
    public ModelAndView getAllPostForUser() {
        List<PostDto> postDto = postService.getAllVerifiedPost();
        //this.postService.getAllPost().stream().
        // filter(PostDto::isVerified).collect(Collectors.toList());
        ModelAndView mav = new ModelAndView("user-view-all-post");
        mav.addObject("postData", postDto);
        return mav;
    }

    @GetMapping("user/post/{postId}")
    public ModelAndView getPostBYPostId(@PathVariable Long postId, Principal principal) {
        ModelAndView mav = new ModelAndView("view-post");
        PostDto postDto = postService.getPostById(postId);
        mav.addObject("postid", postDto);
        UserDto userDto = userService.findUserByEmail(principal.getName());

        boolean isBookMarked = false;
        List<PostDto> bookMarkedPostsList = bookmarkService.getAllBookMarkedPost(userDto);
        for (PostDto bookmarkpost : bookMarkedPostsList) {
            if (bookmarkpost.getId() == postId) {
                isBookMarked = true;
                break;
            }
        }
        mav.addObject("isBookMarked", isBookMarked);
        return mav;
    }

    @GetMapping("user/add-post")
    public ModelAndView addPost() {
        PostDto postDto = new PostDto();
        CategoryDto categoryDto = new CategoryDto();
        ModelAndView modelAndView = new ModelAndView("add-post");
        modelAndView.addObject("postdto", postDto);
        List<CategoryDto> categoryDtos = this.categoryService.findAllCategoryIncludeChildren();
        modelAndView.addObject("categories",categoryDtos);
        return modelAndView;
    }

    @PostMapping("user/save-post")
    public String saveThePost(@ModelAttribute PostDto post, Principal principal) {
        return this.postService.savePost(post, principal.getName(),post.getCategoryDto().getName());
    }


    @GetMapping("/admin/post/verification")
    public String updateVerification(@RequestParam("postId") long postId, @RequestParam("isVerified") boolean isVerified) {
        this.postService.updateVerification(postId, isVerified);
        return "redirect:/admin/all-post";
    }

    @GetMapping("user/my-post")
    public ModelAndView getPostByUserId(Principal principal) {
       //postService.getAllPost().stream().
        //filter(s->s.getId()==UserService.toUser(userDto).getId()).toList();
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<PostDto> postDto = postService.getAllPostByUser(UserConvertor.toUser(userDto));
        ModelAndView modelAndView = new ModelAndView("user-view-all-post");
        modelAndView.addObject("postData", postDto);

        return modelAndView;
    }


    @GetMapping("user/all-post/a")
    public ModelAndView displayPaginatedPosts(@RequestParam("pageNo") int pageNo) {
        Page<Post> paginatedPostList = postService.findPaginatedPost(pageNo, 10);
        Page<Post> postList = postService.findPaginatedPost(pageNo, 10);

        ModelAndView modelAndView = new ModelAndView("user-view-all-post");
        modelAndView.addObject("currentPage", pageNo);
        modelAndView.addObject("totalPages", paginatedPostList.getTotalPages());
        modelAndView.addObject("totalItems", paginatedPostList.getTotalElements());
        modelAndView.addObject("currentPage", postList);
        return modelAndView;

    }

}
