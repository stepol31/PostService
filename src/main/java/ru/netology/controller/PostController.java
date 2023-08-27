package ru.netology.controller;

import org.springframework.web.bind.annotation.*;
import ru.netology.dto.PostDto;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public PostDto getById(@PathVariable long id) {
        Post post = service.getById(id);
        return mapToDto(post);
    }

    @GetMapping
    public List<PostDto> all() {
        return service.all().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public PostDto save(@RequestBody PostDto post) {
        Post model = mapToModel(post);
        Post result = service.save(model);

        return mapToDto(result);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }

    private PostDto mapToDto(Post post) {
        return new PostDto(post.getId(), post.getContent());
    }

    private Post mapToModel(PostDto postDto) {
        if (postDto.getId() == null) {
            return new Post(postDto.getContent());
        }
        return new Post(postDto.getId(), postDto.getContent());
    }
}
