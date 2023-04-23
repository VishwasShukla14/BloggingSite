package com.training.bloggingsite.services.impl;

import com.training.bloggingsite.dtos.PostDto;
import com.training.bloggingsite.entities.Category;
import com.training.bloggingsite.entities.Post;
import com.training.bloggingsite.entities.Role;
import com.training.bloggingsite.entities.User;
import com.training.bloggingsite.repositories.CategoryRepository;
import com.training.bloggingsite.repositories.PostRepository;
import com.training.bloggingsite.repositories.UserRepository;
import com.training.bloggingsite.services.interfaces.PostService;
import com.training.bloggingsite.utils.CriteriaQueryBuilder;
import com.training.bloggingsite.utils.DefaultValue;
import com.training.bloggingsite.utils.PostConvertor;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    CriteriaQueryBuilder cb;

    @Autowired
    CategoryRepository categoryRepository;

    Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public String savePost(PostDto post, String userEmail, String categoryName) {
        List<User> user = this.cb.getResultWhereColumnEqual("email",userEmail,User.class);
        List<Category> category = cb.getResultWhereColumnEqual("name", categoryName, Category.class);
        Post postToBeInserted = PostConvertor.toPost(post);
        postToBeInserted.setCategory(category.get(0));
        postToBeInserted.setUser(user.get(0));
        List<Role> roles = user.get(0).getRoles().stream().toList();
        if (roles.get(0).getName().equals(DefaultValue.ADMIN)) {
            postToBeInserted.setVerified(true);
             this.postRepository.save(postToBeInserted);
            logger.info("Post created as : " + postToBeInserted.getTitle() + " by " + user.get(0).getName());
            return "redirect:/admin/home";
        } else {
            postToBeInserted.setVerified(false);
               this.postRepository.save(postToBeInserted);
            logger.info("Post created as : " + postToBeInserted.getTitle() + " by " + user.get(0).getName());
            return "redirect:/user/home";
        }
    }

    @Override
    public PostDto findPostById(long id) {
        logger.info("find post by id");
        return PostConvertor.toPostDto(cb.getResultWhereColumnEqual("id", id, Post.class).get(0));

    }

    @Override
    public List<PostDto> findAllPostByUser(User user) {

        List<PostDto> postDtos = new ArrayList<>();
        List<Post> postByUserId= cb.getResultWhereColumnEqual("user",user,Post.class);
        for (Post post : postByUserId)
            postDtos.add(PostConvertor.toPostDto(post));


        return postDtos;
    }

    @Override
    public void deletePost(long id) {
        this.postRepository.deleteById(id);
        logger.info("Post Deleted with id  : " + id);
    }

    @Override
    public void updateVerification(long postId, boolean isVerified) {
        Post post = this.postRepository.findById(postId).get();
        this.postRepository.updateVerificationStatus(postId, !isVerified);
    }

    @Override
    public <T> List<PostDto> findPaginatedPosts(int offset, int limit,String columnName,T value) {
        List<PostDto> postDtos = new ArrayList<>();
        List<Post> post = cb.getPaginatedData(offset,limit,"isVerified",value,Post.class);
        for (Post p : post) {
            postDtos.add(PostConvertor.toPostDto(p));
        }
        return postDtos;
    }

    @Override
    public <T> int findTotalPages(String columnName,T value) {
        return cb.getCount(columnName,value,Post.class);
    }


}
