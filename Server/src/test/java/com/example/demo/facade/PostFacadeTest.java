package com.example.demo.facade;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostFacadeTest {

    User user = createTestUser();
    Post post = createTestPost(user);
    PostFacade postFacade = new PostFacade();
    PostDTO expectedDto = new PostDTO();

    @Test
    void postToPostDTO() {
        //then
        PostDTO dto = postFacade.PostToPostDTO(post);

        expectedDto.setUsersLiked(post.getLikedUsers());
        expectedDto.setLikes(post.getLikes());
        expectedDto.setCaption(post.getCaption());
        expectedDto.setUsername(post.getUser().getUsername());
        expectedDto.setLocation(post.getLocation());
        expectedDto.setId(post.getId());
        expectedDto.setTitle(post.getTitle());

        //validation
        assertEquals(expectedDto, dto);
    }

    private Post createTestPost(User user) {
        Post post = new Post();
        post.setCaption("Test Caption");
        post.setLocation("Test Location");
        post.setTitle("Test Title");
        post.setId(1L);
        post.setUser(user);
        return post;
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testUser@example.com");
        user.setUsername("testUser");
        return user;
    }

}