package com.tvntvn.letsplay.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.util.concurrent.RateLimiter;

// import com.google.gson.Gson;
// import com.tvntvn.letsplay.util.ResponseFormatter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitFilter extends OncePerRequestFilter {

  private final RateLimiter rateLimiter = RateLimiter.create(5);

  // @Autowired private ResponseFormatter formatter;

  // private ResponseEntity<Object> sendError(Exception e) {
  //   return formatter.format(e.getMessage(), HttpStatus.BAD_REQUEST);
  // }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, java.io.IOException {

    try {
      if (rateLimiter.tryAcquire()) {
        filterChain.doFilter(request, response);
      } else {
        response.setStatus(429);
        response.setContentType("text/plain");
        response.getWriter().write("rate limit exceeded");
      }
    } catch (Exception e) {
      // ApiError error = new ApiError(HttpStatus.NOT_ACCEPTABLE.value(), "something went really wrong", e);
      response.setStatus(406);
      response.setContentType("text/plain");
      // PrintWriter writer = response.getWriter();
      // writer.print(error.toString());
      // writer.flush();
      response
          .getWriter()
          .write(
              "something went wrong, try again later\n\n"
                  + e.getLocalizedMessage());
    }
  }
}
