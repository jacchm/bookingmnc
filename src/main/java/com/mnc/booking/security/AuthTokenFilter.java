package com.mnc.booking.security;

import com.mnc.booking.security.util.HeaderMapRequestWrapper;
import com.mnc.booking.security.util.JwtTokenProvider;
import com.mnc.booking.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

  private static final String USERNAME_HEADER = "username";

  private JwtTokenProvider jwtUtils;
  private UserDetailsServiceImpl userDetailsService;
  @Value("${authTokenFilter.enabled}")
  private boolean isEnabled;
  private AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Autowired
  private void setJwtUtils(final JwtTokenProvider jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @Autowired
  private void setUserDetailsService(final UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  void setup() {
    log.info("AuthTokenFilter is enabled={}", isEnabled);
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {
    final HeaderMapRequestWrapper wrappedRequest = new HeaderMapRequestWrapper(request);
    if (isEnabled) {
      try {
        final String jwt = parseJwt(request);
        if (Objects.nonNull(jwt) && jwtUtils.validateToken(jwt)) {
          final String username = jwtUtils.getUserNameFromJwtToken(jwt);
          final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          final UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
          if (antPathMatcher.match("/me/**", request.getServletPath())) {
            wrappedRequest.addHeader(USERNAME_HEADER, username);
          }
        }
      } catch (Exception ex) {
        log.error("Cannot set user authentication: {}", ex.getMessage());
      }
      filterChain.doFilter(wrappedRequest, response);
    }
  }

  private String parseJwt(final HttpServletRequest request) {
    final String headerAuth = request.getHeader("Authorization");
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }

}
