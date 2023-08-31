package com.tvntvn.letsplay.filter;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.util.concurrent.RateLimiter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitFilter extends OncePerRequestFilter {

  private final RateLimiter rateLimiter = RateLimiter.create(5);

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      if (rateLimiter.tryAcquire()) {
        filterChain.doFilter(request, response);
      } else {
        response.setStatus(429);
        response.setContentType("text/plain");
        response.getWriter().write("rate limit exceeded");
      }
    } catch (Exception e) {
      response.setStatus(403);
      response.setContentType("text/plain");
      response
          .getWriter()
          .write("something went wrong, try again later\n\n" + e.getLocalizedMessage());
    }
  }
}
