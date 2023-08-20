package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PostRepository {
    ConcurrentHashMap<Long, Post> storage = new ConcurrentHashMap<>();
    AtomicLong id = new AtomicLong();

    public List<Post> all() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.of(storage.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0) {
            long key = id.incrementAndGet();
            post.setId(key);
            return storage.put(key, post);
        } else if (storage.containsKey(post.getId())) {
            storage.put(post.getId(), post);
            return post;
        } else {
            throw new RuntimeException("Post can't be saved");
        }
    }

    public Post removeById(long id) {
        return storage.remove(id);
    }
}
