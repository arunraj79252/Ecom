package com.assessment.ecom.auth;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class SwaggerRoleFilter {

    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    public SwaggerRoleFilter(HttpServletRequest request, JwtUtil jwtUtil) {
        this.request = request;
        this.jwtUtil = jwtUtil;
    }

    public OpenAPI filterOpenAPI(OpenAPI openAPI) {
        System.out.println("say...");
        String role = getUserRole();
        Paths paths = openAPI.getPaths();
        Iterator<Map.Entry<String, PathItem>> iterator = paths.entrySet().iterator();

        // Remove paths for non-authenticated users
        if (role.equals("PUBLIC")) {
            iterator.forEachRemaining(entry -> {
                PathItem pathItem = entry.getValue();
                filterPublicMethods(pathItem);
                if (pathItem.readOperations().isEmpty()) {
                    iterator.remove();
                }
            });
        }
        return openAPI;
    }

    private void filterPublicMethods(PathItem pathItem) {
        pathItem.setDelete(null); // Remove DELETE for public users
        pathItem.setPut(null);    // Remove PUT for public users
        pathItem.setPatch(null);  // Remove PATCH for public users
    }

    private String getUserRole() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                List<String> roles = jwtUtil.extractRoles(token);
                if (roles != null && roles.contains("ROLE_ADMIN")) {
                    return "ADMIN";
                }
                return "USER"; // or return "PUBLIC" if no valid role found
            }
        }
        return "PUBLIC"; // Default role if no token or invalid token
    }
}
