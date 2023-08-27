package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {
    ConcurrentHashMap<Long, Post> storage = new ConcurrentHashMap<>();
    AtomicLong id = new AtomicLong();

    public List<Post> all() {
        return storage.values().stream()
                .filter(post -> !post.isRemoved())
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        Post post = storage.get(id);
        if (post != null && !post.isRemoved()) {
            return Optional.of(post);
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0) {
            long key = id.incrementAndGet();
            post.setId(key);
            return storage.put(key, post);
        } else if (storage.containsKey(post.getId()) && !storage.get(post.getId()).isRemoved()) {
            storage.put(post.getId(), post);
            return post;
        } else {
            throw new RuntimeException("Post can't be saved");
        }
    }

    public Post removeById(long id) {
        Post post = storage.get(id);
        if (post != null) {
            post.setRemoved(true);
        }
        return post;
    }
}
