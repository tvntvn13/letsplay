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

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      System.out.println("i got here bro, exceptionFilter");

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("text/plain");
      response
          .getWriter()
          .write("something went wrong, try again later\n\n" + e.getLocalizedMessage());
    }
  }
}
