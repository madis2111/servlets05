package ru.netology.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;


  @Override
  public void init() {

    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.netology");

    controller = context.getBean(PostController.class);


//    final var repository = new PostRepository();
//    final var service = new PostService(repository);
//    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {

    try {
      final String path = req.getRequestURI();
      final var method = req.getMethod();
      if (method.equals("GET") && path.equals("/api/posts") && req.getParameter("id") == null) {
        controller.all(resp);
        return;
      }
      if (method.equals("GET") && path.equals("/api/posts")) {
        final var id = Long.parseLong(req.getParameter("id"));
        controller.getById(id, resp);
        return;
      }
      if (method.equals("POST") && path.equals("/api/posts")) {
        System.out.println();
        String idAsString = req.getParameter("id");
        long id = Long.parseLong(idAsString);
        if (id == 0) {
          controller.save(req.getReader(), resp);
        } else {
          controller.edit(req, resp, id);
        }
        return;
      }

      if (method.equals("DELETE") && path.matches("/api/posts")) {
        final var id = Long.parseLong(req.getParameter("id"));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
