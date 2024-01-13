package exercise.controller;

import exercise.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("") // Список страниц
    public List<Comment> index() {
        return commentRepository.findAll();
    }

    @PostMapping("") // Создание страницы
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestBody Comment comment) {
        commentRepository.save(comment);
        return comment;
    }

    @GetMapping("/{id}") // Вывод страницы
    public Comment show(@PathVariable String id) {
        return commentRepository
                .findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    @PutMapping("/{id}") // Обновление страницы
    public Comment update(@PathVariable String id, @RequestBody Comment data) {
        var maybeComment = commentRepository
                .findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));;
        maybeComment.setPostId(data.getPostId());
        maybeComment.setBody(data.getBody());
        commentRepository.save(maybeComment);
        return data;
    }

    @DeleteMapping("/{id}") // Удаление страницы
    public void destroy(@PathVariable String id) {
        commentRepository.deleteById(Long.parseLong(id));
    }
}