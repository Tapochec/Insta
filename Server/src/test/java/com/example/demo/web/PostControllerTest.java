package com.example.demo.web;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.AssertJUnit.assertEquals;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.facade.PostFacade;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.PostService;
import com.example.demo.validations.ResponseErrorValidation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.security.Principal;



@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private PostFacade postFacade;

    @MockBean
    private ResponseErrorValidation responseErrorValidation;

    @InjectMocks
    private PostController postController;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

        PostService postServiceMock = new PostService(postRepository, userRepository, imageRepository);
        Mockito.when(postServiceMock.createPost(any(PostDTO.class), any(Principal.class))).thenReturn(new Post());
        Mockito.when(postServiceMock.getUserByPrincipal(any(Principal.class))).thenReturn(new User());

        Mockito.when(postService.createPost(any(PostDTO.class), any(Principal.class))).thenCallRealMethod();
        Mockito.when(postService.getUserByPrincipal(any(Principal.class))).thenCallRealMethod();
    }

    @Test
    void createPost_ValidPost() throws Exception {
    }

    @Test
    void getAllPosts() throws Exception {
        Mockito.when(this.postService.getAllPosts());

        mockMvc.perform(get("api/post/all")).andExpect(status().isOk());
    }

    @Test
    void getAllPostsForUser() throws Exception {
    }

    @Test
    void likePost() {
    }

    @Test
    void deletePost() {
    }
}