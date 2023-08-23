package com.tvntvn.letsplay.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Order(2)
@Component
public class ExceptionFilter extends OncePerRequestFilter {

  // @Autowired private ResponseFormatter formatter;

  // private ResponseEntity<Object> sendError(Exception e) {
  //   return formatter.format(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
  // }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      System.out.println("i got here bro, exceptionFilter");

      // ApiError error =
      //     new ApiError(HttpStatus.NOT_ACCEPTABLE.value(), "something went really wrong", e);
      // ResponseEntity<Object> errorResponse = sendError(e);
      response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
      response.setContentType("text/plain");
      // PrintWriter writer = response.getWriter();
      // writer.print(error);
      // writer.flush();
      response
          .getWriter()
          .write("something went wrong, try again later\n\n" + e.getLocalizedMessage());

      // response.setContentType("text/plain");
      // response.getWriter().write("error: unauthorized\n" + e.getMessage().split(":")[2].trim());
      // response
      //     .getWriter()
      //     .write(formatter.format(e.getMessage(), HttpStatus.NOT_ACCEPTABLE));
      // formatter.format(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
  }
}
