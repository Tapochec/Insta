package com.example.demo.services;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.ImageModel;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageRepository imageRepository;

    PostService postService;

    PostDTO postDTO = createTestPostDTO();
    User user = createTestUser();
    Post post = createTestPost();
    List<Post> posts = initPosts();

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository, imageRepository);
    }

    @Test
    void createPost() {

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
        post.setUser(user);

        //when
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));

        //then
        Post createdPost = postService.createPost(postDTO, principal);

        //validation
        assertNotNull(createdPost);
        assertEquals(1L, createdPost.getId());
        assertEquals("Test Caption", createdPost.getCaption());
        assertEquals("testUser@example.com", createdPost.getUser().getEmail());
    }

    @Test
    void getAllPosts() {
        //when
        when(postRepository.findAllByOrderByCreatedDateDesc()).thenReturn(posts);


        //then
        List<Post> AllPosts = postService.getAllPosts();

        //validation
        assertNotNull(AllPosts);
        assertEquals(2, AllPosts.size());
    }

    @Test
    void getPostById() {

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
        post.setUser(user);

        //when
        when(postRepository.findPostByIdAndUser(1L, user)).thenReturn(Optional.ofNullable(post));
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));

        //then
        Post getPost = postService.getPostById(1L, principal);

        //validation
        assertEquals(1L, getPost.getId());
        assertEquals("Test Caption", getPost.getCaption());
        assertEquals("testUser@example.com", getPost.getUser().getEmail());
    }

    @Test
    void getAllPostForUser() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
        posts.get(0).setUser(user);
        posts.get(1).setUser(user);

        //when
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.findAllByUserOrderByCreatedDateDesc(user)).thenReturn(posts);


        //then
        List<Post> AllUserPosts = postService.getAllPostForUser(principal);

        //validation
        assertNotNull(AllUserPosts);
        assertEquals(2, AllUserPosts.size());
    }

    @Test
    void likePost() {
        Principal principal = mock(Principal.class);
        post.setLikes(0);

        //when
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);


        //then
        Post likedPost = postService.likePost(post.getId(), user.getUsername());

        //validation
        assertEquals(1, likedPost.getLikes());
        assertTrue(likedPost.getLikedUsers().contains(user.getUsername()));
    }

    @Test
    void deletePost() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");

        post.setUser(user);
        ImageModel image = new ImageModel();
        image.setPostId(1L);

        //when
        when(postRepository.findPostByIdAndUser(1L, user)).thenReturn(Optional.ofNullable(post));
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));
        when(imageRepository.findByPostId(1L)).thenReturn(Optional.of(image));

        //then
        postService.deletePost(post.getId(), principal);

        //validation
        verify(postRepository, times(1)).delete(post);
        verify(imageRepository, times(1)).delete(image);
    }

    private Post createTestPost() {
        Post post = new Post();
        post.setCaption("Test Caption");
        post.setLocation("Test Location");
        post.setTitle("Test Title");
        post.setId(1L);
        return post;
    }

    private PostDTO createTestPostDTO() {
        PostDTO postDTO = new PostDTO();
        postDTO.setCaption("Test Caption");
        postDTO.setLocation("Test Location");
        postDTO.setTitle("Test Title");
        return postDTO;
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testUser@example.com");
        return user;
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

}