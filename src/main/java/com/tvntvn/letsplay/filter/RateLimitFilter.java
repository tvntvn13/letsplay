package com.tvntvn.letsplay.filter;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.tvntvn.letsplay.model.ApiError;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitFilter extends OncePerRequestFilter {

  private final RateLimiter rateLimiter = RateLimiter.create(20);

  private Gson gson = new Gson();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    ApiError apierror = new ApiError();
    String apiErrorString;

    try {
      if (rateLimiter.tryAcquire()) {
        filterChain.doFilter(request, response);
      } else {
        response.setStatus(429);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        apierror.setPayload("ratelimit exceeded");
        apierror.setStatus(429);
        apierror.setDetail("TOO MANY REQUESTS");

        apiErrorString = this.gson.toJson(apierror);
        response.getWriter().print(apiErrorString);
        response.getWriter().flush();
      }
    } catch (Exception e) {
      response.setStatus(403);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      apierror.setPayload(e.getLocalizedMessage());
      apierror.setStatus(403);
      apierror.setDetail("FORBIDDEN");

      apiErrorString = this.gson.toJson(apierror);
      response.getWriter().print(apiErrorString);
      response.getWriter().flush();
    }
  }
}
