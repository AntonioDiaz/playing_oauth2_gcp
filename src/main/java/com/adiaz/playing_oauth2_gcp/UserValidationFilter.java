package com.adiaz.playing_oauth2_gcp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class UserValidationFilter extends OncePerRequestFilter {

  @Value("#{'${users_email}'.split(';')}")
  private List<String> usersEmailsList;

  private final JwtDecoder jwtDecoder;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("inside the Filter");
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
    } else {
      final String token = header.split(" ")[1].trim();
      Jwt jwt = jwtDecoder.decode(token);
      String email = jwt.getClaims().get("email").toString();
      log.info("Email -> {}", email);
      if (!usersEmailsList.contains(email)) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
      } else {
        filterChain.doFilter(request, response);
      }
    }
  }
}
