package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String API_POSTS = "/api/posts";
  private static final String GET = "GET";
  private static final String POST = "POST";
  private static final String DELETE = "DELETE";
  private static final String ID = "id";


  @Override
  public void init() {

    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.netology");

    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {

    try {
      final String requestedPath = req.getRequestURI();
      final var method = req.getMethod();
      if (method.equals(GET) && requestedPath.equals(API_POSTS) && req.getParameter(ID) == null) {
        controller.all(resp);
        return;
      }
      if (method.equals(GET) && requestedPath.equals(API_POSTS)) {
        final var id = Long.parseLong(req.getParameter(ID));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(POST) && requestedPath.equals(API_POSTS)) {
        System.out.println();
        String idAsString = req.getParameter(ID);
        long id = Long.parseLong(idAsString);
        if (id == 0) {
          controller.save(req.getReader(), resp);
        } else {
          controller.edit(req, resp, id);
        }
        return;
      }

      if (method.equals(DELETE) && requestedPath.matches(API_POSTS)) {
        final var id = Long.parseLong(req.getParameter(ID));
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
