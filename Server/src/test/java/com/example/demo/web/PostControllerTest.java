package com.example.demo.web;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import com.example.demo.facade.PostFacade;
import com.example.demo.services.PostService;
import com.example.demo.validations.ResponseErrorValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
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


    PostController postController;



    @BeforeEach
    void setUp() {
      postController = new PostController(postService, postFacade, responseErrorValidation);

    }

    @Test
    void createPost_ValidPost() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        BindingResult bindingResult = null;
        Principal principal = null;


        //when
        Mockito.when(postService.createPost(any(PostDTO.class), any())).thenReturn( new Post());
        Mockito.when(responseErrorValidation.mapValidationService(any())).thenReturn(null);
        Mockito.when(postFacade.PostToPostDTO(any())).thenReturn(postDTO);

        //then
        ResponseEntity<Object> response = postController.createPost(postDTO, bindingResult, principal);

        //validation

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.hasBody());

    }
}