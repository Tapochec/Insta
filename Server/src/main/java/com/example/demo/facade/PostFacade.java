package com.example.demo.facade;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDTO PostToPostDTO(Post post) {
        PostDTO postDTO = PostDTO.builder().build();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setCaption(post.getCaption());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setLikes(post.getLikes());
        postDTO.setLocation(post.getLocation());
        postDTO.setUsersLiked(post.getLikedUsers());

        return postDTO;
    }

}
