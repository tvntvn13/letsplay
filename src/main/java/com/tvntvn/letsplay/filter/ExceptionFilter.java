package com.tvntvn.letsplay.filter;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.tvntvn.letsplay.model.ApiError;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Order(2)
@Component
public class ExceptionFilter extends OncePerRequestFilter {

  private Gson gson = new Gson();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {

      ApiError apierror =
          new ApiError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.name(), e);

      String apierrorString = this.gson.toJson(apierror);

      PrintWriter out = response.getWriter();

      response.setContentType("application/json");
      response.setStatus(403);
      response.setCharacterEncoding("UTF-8");
      out.print(apierrorString);
      out.flush();
    }
  }
}
