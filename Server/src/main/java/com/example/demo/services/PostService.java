package com.example.demo.services;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.ImageModel;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.exceptions.PostNotFoundException;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) { // Создание нового поста в базе данных
        User user = getUserByPrincipal(principal); // Получение пользователя из `Principal`, который представляет текущего авторизованного пользователя
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0); // Установка начального количества лайков равного 0.

        LOG.info("Сохранение поста от пользователя: {}", user.getEmail());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() { // Получение списка всех постов из бд в порядке убывания даты их создания
        return postRepository.findAllByOrderByCreatedDateDesc();

    }

    public Post getPostById(Long postId, Principal principal) { // Получение поста по его идентификатору
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user).orElseThrow(() -> new PostNotFoundException("Пост не может быть найден для пользователя " + user.getUsername()));
    }

    public List<Post> getAllPostForUser(Principal principal) { // найти все посты юзера
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long postId, String username) { // Добавление/удаление лайка к посту
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Пост не может быть найден"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(user -> user.equals(username)).findAny();

        if (userLiked.isPresent()) { //  Проверка, есть ли уже лайк от текущего пользователя
            post.setLikes(post.getLikes() - 1); // если да, то убираем лайк
            post.getLikedUsers().remove(username); // если да, то убираем юзера из лайкнувших
        } else {
            post.setLikes(post.getLikes() + 1); // если нет, ставим лайк и добавляем человека в лайкнувшие
            post.getLikedUsers().add(username);
        }

        return postRepository.save(post); // сохраняем пост с обновленными внутренними данными
    }

    public void deletePost(Long postId, Principal principal) { // ну удаляем пост, чё
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId()); // смотрим, есть ли картинки, связанные с постом
        postRepository.delete(post); // удаляем пост
        imageModel.ifPresent(imageRepository::delete); // удаляем его картинки, если они есть

    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь " + username + " не найден"));
    }
}
