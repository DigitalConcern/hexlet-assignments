package exercise;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/posts") // Список страниц
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        List<Post> postsToResponse = posts.stream().skip((long) (page - 1) * limit).limit(limit).toList();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(postsToResponse.size())).body(postsToResponse);
    }

    @PostMapping("/posts") // Создание страницы
    public ResponseEntity<List<Post>> create(@RequestBody Post Post) {
        posts.add(Post);
        return ResponseEntity.created(URI.create("/posts/" + Post.getId())).body(posts);
    }

    @GetMapping("/posts/{id}") // Вывод страницы
    public ResponseEntity<Post> show(@PathVariable String id) {
        var Post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        return Post.map(post -> ResponseEntity.ok().body(post)).orElse(ResponseEntity.status(404).body(null));
    }

    @PutMapping("/posts/{id}") // Обновление страницы
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
        var maybePost = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (maybePost.isPresent()) {
            var Post = maybePost.get();
            Post.setId(data.getId());
            Post.setTitle(data.getTitle());
            Post.setBody(data.getBody());
            return ResponseEntity.ok().body(data);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
