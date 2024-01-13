package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("") // Список страниц
    public List<Post> index() {
        return postRepository.findAll();
    }

    @PostMapping("") // Создание страницы
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        postRepository.save(post);
        return post;
    }

    @GetMapping("/{id}") // Вывод страницы
    public Post show(@PathVariable String id) {
        return postRepository
               .findById(Long.valueOf(id))
               .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    @PutMapping("/{id}") // Обновление страницы
    public Post update(@PathVariable String id, @RequestBody Post data) {
        var maybePost = postRepository
                .findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));;
        maybePost.setTitle(data.getTitle());
        maybePost.setBody(data.getBody());
        postRepository.save(maybePost);
        return data;
    }

    @DeleteMapping("/{id}") // Удаление страницы
    public void destroy(@PathVariable String id) {
        postRepository.deleteById(Long.parseLong(id));
        commentRepository.deleteByPostId(Long.parseLong(id));
    }

}