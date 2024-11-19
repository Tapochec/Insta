package com.example.demo.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import com.example.demo.facade.PostFacade;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.PostService;
import com.example.demo.validations.ResponseErrorValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    @Mock
    private PostService postService;
    @Mock
    private PostFacade postFacade;
    @Mock
    private ResponseErrorValidation responseErrorValidation;

    PostController postController;

    List<Post> posts = initPosts();
    List<PostDTO> postDTOS = initPostDTOS();


    @BeforeEach
    void setUp() {
        postController = new PostController(postService, postFacade, responseErrorValidation);

    }

    @Test
    void createPost_ValidPost() throws Exception {
        BindingResult bindingResult = null;
        Principal principal = null;

        //when
        Mockito.when(postService.createPost(any(), any())).thenReturn(new Post());
        Mockito.when(responseErrorValidation.mapValidationService(any())).thenReturn(null);
        Mockito.when(postFacade.PostToPostDTO(any())).thenReturn(postDTOS.get(0));

        //then
        ResponseEntity<Object> response = postController.createPost(postDTOS.get(0), bindingResult, principal);

        //validation
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.hasBody());

    }

    @Test
    void getAllPosts_Return_OK_Status() throws Exception {
        //when
        when(postService.getAllPosts()).thenReturn(posts);
        when(postFacade.PostToPostDTO(posts.get(0))).thenReturn(postDTOS.get(0));
        when(postFacade.PostToPostDTO(posts.get(1))).thenReturn(postDTOS.get(1));

        //then
        ResponseEntity<List<PostDTO>> response = postController.getAllPosts();

        //validation
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDTOS, response.getBody());
    }

    @Test
    void getUserPosts_Return_OK_Status() throws Exception {
        Principal principal = null;

        //when
        when(postService.getAllPostForUser(principal)).thenReturn(posts);
        when(postFacade.PostToPostDTO(posts.get(0))).thenReturn(postDTOS.get(0));
        when(postFacade.PostToPostDTO(posts.get(1))).thenReturn(postDTOS.get(1));

        //then
        ResponseEntity<List<PostDTO>> response = postController.getAllPostsForUser(principal);

        //validation
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(initPostDTOS(), response.getBody());
    }

    @Test
    void likePost_Return_OK() throws Exception {
        String postId = "1";
        String username = "test";

        //when
        Mockito.when(postService.likePost(Long.parseLong(postId), username)).thenReturn(posts.get(0));
        Mockito.when(postFacade.PostToPostDTO(posts.get(0))).thenReturn(postDTOS.get(0));

        //then
        ResponseEntity<PostDTO> response = postController.likePost(postId, username);

        //validation
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDTOS.get(0), response.getBody());

    }

    @Test
    void deletePost_Return_OK() throws Exception {
        String postId = "1";
        Principal principal = null;

        //then
        ResponseEntity<MessageResponse> response = postController.deletePost(postId, principal);

        //validation
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    private List<Post> initPosts() {
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1L);
        Post post2 = new Post();
        post2.setId(2L);
        posts.add(post1);
        posts.add(post2);
        return posts;
    }

    private List<PostDTO> initPostDTOS() {
        List<PostDTO> postDTOS = new ArrayList<>();
        PostDTO postDTO1 = new PostDTO();
        postDTO1.setId(1L);
        postDTO1.setUsername("test");
        PostDTO postDTO2 = new PostDTO();
        postDTO2.setId(2L);
        postDTOS.add(postDTO1);
        postDTOS.add(postDTO2);
        return postDTOS;
    }

}