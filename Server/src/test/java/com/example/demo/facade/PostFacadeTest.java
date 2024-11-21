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

    @Test
    void postToPostDTO() {
        //then
        PostDTO dto = postFacade.PostToPostDTO(post);

        //validation
        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getTitle(), dto.getTitle());
        assertEquals(post.getCaption(), dto.getCaption());
        assertEquals(post.getUser().getUsername(), dto.getUsername());
        assertEquals(post.getLikes(), dto.getLikes());
        assertEquals(post.getLocation(), dto.getLocation());
        assertEquals(post.getLikedUsers(), dto.getUsersLiked());
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