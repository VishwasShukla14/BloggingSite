package com.training.bloggingsite.services.impl;

import com.training.bloggingsite.dtos.PostDto;
import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.entities.BookMark;
import com.training.bloggingsite.entities.Post;
import com.training.bloggingsite.entities.User;
import com.training.bloggingsite.repositories.BookMarkRepository;
import com.training.bloggingsite.services.interfaces.BookmarkService;
import com.training.bloggingsite.services.interfaces.PostService;
import com.training.bloggingsite.services.interfaces.UserService;
import com.training.bloggingsite.utils.CriteriaQueryBuilder;
import com.training.bloggingsite.utils.PostConvertor;
import com.training.bloggingsite.utils.UserConvertor;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {
    @Autowired
    BookMarkRepository bookMarkRepository;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    CriteriaQueryBuilder cb;
    Logger logger = LoggerFactory.getLogger(BookmarkServiceImpl.class);

    @Override
    @Transactional
    public List<PostDto> getAllBookMarkedPost(UserDto userDto) {
        List< BookMark > bookMarkList=bookMarkRepository.findAll().stream().
                filter(B->B.getUser().getId()==userDto.getId()).toList();
        List<Post> post=bookMarkList.stream().map(B->B.getPost()).toList();
        List<PostDto> postDtos=new ArrayList<>();
        for(Post p:post){
            postDtos.add(PostConvertor.toPostDto(p));
        }
        logger.info("Bookmarked Posts fetched : "+postDtos);
        return postDtos;
    }

    @Override
    public PostDto deleteBookMarkedPostByPostID(UserDto userDto, PostDto postDto) {
        List<BookMark> bookMarkList = cb.getAll(BookMark.class).stream().toList();
        bookMarkList = bookMarkList.stream().filter(s -> s.getPost().getId() == postDto.getId()).toList();
        //bookMarkRepository.deleteById(bookMarkList.get(0).getId());
        cb.deleteById("id", bookMarkList.get(0).getId(), BookMark.class);
        logger.info("Bookmarked post deteted : " + postDto);
        return postDto;
    }

    @Override
    public void addBookMarkedPost(PostDto postDto,UserDto userDto) {
        BookMark bookMark=new BookMark();
        bookMark.setUser(UserConvertor.toUser(userDto));
        bookMark.setPost(PostConvertor.toPost(postDto));
        logger.info("Bookmarked post added : "+postDto);
        bookMarkRepository.save(bookMark);
    }

    @Override
    public boolean isBookMarked(UserDto userDto, long postId) {
        boolean isBookMarked = false;
        List<PostDto> bookMarkedPostsList = getAllBookMarkedPost(userDto);
        isBookMarked = bookMarkedPostsList.stream().anyMatch(B->B.getId()==postId);
        logger.info("Bookmarked Check : "+isBookMarked+" for postId :"+postId+" user :"+userDto);
        return isBookMarked;
    }

    @Override
    public void changeBookMarkStatus(long postId, boolean isBookMarked, Principal principal) {
        UserDto userDto = userService.findUserByEmail(principal.getName());
        if(isBookMarked){
            logger.info("Changed bookmarked status for post : "+postId+" user with email : "+principal.getName()+" as "+!isBookMarked);
            this.deleteBookMarkedPostByPostID(userDto,postService.findPostById(postId));
        }else {
            logger.info("Changed bookmarked status for post : "+postId+" user with email : "+principal.getName()+" as "+!isBookMarked);
            this.addBookMarkedPost(postService.findPostById(postId), userDto);
        }
    }
}
