package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";

    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (!path.contains("/api/posts")) {
                throw new RuntimeException("path is not allowed");
            }

            long id = 0;
            if (path.matches("/api/posts/\\d+")) {
                id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            }

            switch (method) {
                case GET:
                    if (id != 0) {
                        controller.getById(id, resp);
                    } else {
                        controller.all(resp);
                    }
                    break;
                case POST:
                    controller.save(req.getReader(), resp);
                    break;
                case DELETE:
                    controller.removeById(id, resp);
                    break;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

