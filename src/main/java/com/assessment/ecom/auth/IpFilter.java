package com.assessment.ecom.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class IpFilter extends OncePerRequestFilter {

    // Inject the comma-separated IPs
    @Value("${ip-filter.allowed-ips}")
    private String allowedIpsString;

    private List<String> allowedIps;

    @Override
    public void afterPropertiesSet() throws ServletException {
        // Split the comma-separated string into a list
        allowedIps = Arrays.asList(allowedIpsString.split(","));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        System.out.println(clientIp);

        if (!allowedIps.contains(clientIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied: Your IP is not allowed");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
