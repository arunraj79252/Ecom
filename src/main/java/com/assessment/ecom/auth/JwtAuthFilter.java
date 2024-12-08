package com.assessment.ecom.auth;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Define the list of excluded routes with HTTP methods as strings
    private final List<RouteConfig> excludedRoutes = List.of(new RouteConfig("/api/user", "POST"),
            new RouteConfig("/api/user", "GET"), new RouteConfig("/api/auth/**", null),
            new RouteConfig("/swagger-ui/**", "GET"),   // Exclude Swagger UI
            new RouteConfig("/v3/api-docs/**", "GET"),  // Exclude OpenAPI docs
            new RouteConfig("/swagger-ui.html", null),  // Exclude old Swagger UI path (for older versions)
            new RouteConfig("/swagger-resources/**", null) // Exclude Swagger resources
    );

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        log.debug("Incoming request path: {}, method: {}", path, method);

        // Skip JWT validation for excluded routes
        if (shouldExclude(path, method)) {
            log.debug("Skipping JWT validation for excluded route: {}, method: {}", path, method);
            chain.doFilter(request, response);
            return;
        }

        // JWT validation starts here
        String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = authHeader.substring(7); // Extract the token after "Bearer "
        if (!jwtUtil.validateToken(token)) {
            log.warn("Invalid JWT token");
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // Get user details from token
        String username = jwtUtil.extractUsername(token);
        List<String> roles = jwtUtil.extractRoles(token); // Extract roles from JWT token

        // Convert roles to GrantedAuthorities
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        // Create an Authentication object and set it in the SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Log the authorities after setting the authentication context
        log.debug("Authorities: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        log.debug("JWT token validated successfully");
        chain.doFilter(request, response);
    }

    private boolean shouldExclude(String path, String method) {
        return excludedRoutes.stream().anyMatch(routeConfig -> pathMatcher.match(routeConfig.getPath(), path) && (routeConfig.getMethod() == null || routeConfig.getMethod().equalsIgnoreCase(method)));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization logic needed; override required by interface
    }

    @Override
    public void destroy() {
        // No cleanup logic needed; override required by interface
    }

    // Helper class to define route and method exclusions
    @Getter
    private static class RouteConfig {
        private final String path;
        private final String method;

        public RouteConfig(String path, String method) {
            this.path = path;
            this.method = method;
        }

    }
}
