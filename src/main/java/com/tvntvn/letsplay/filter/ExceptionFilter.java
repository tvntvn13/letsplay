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
      //   response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      //   response.setContentType("text/plain");
      // response.getWriter().write("error: unauthorized\n" + e.getMessage().split(":")[2].trim());
      response.getWriter().write("error: this is from the filter\n" + e.getMessage().trim());
    }
  }
}
