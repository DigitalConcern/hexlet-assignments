package exercise.controller.users;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

@RestController
@RequestMapping("/api/users")
public class PostsController {

    private final List<Post> posts = Data.getPosts();

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Post>> getPosts(@PathVariable String userId) {
        return ResponseEntity.ok().body(
                posts.stream().filter(
                        post -> post.getUserId() == Integer.parseInt(userId)).collect(Collectors.toList()
                )
        );
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<Post> setPost(@PathVariable String userId, @RequestBody Post post) {
        post.setUserId(Integer.parseInt(userId));
        posts.add(post);
        return ResponseEntity.status(201).body(post);
    }
}
