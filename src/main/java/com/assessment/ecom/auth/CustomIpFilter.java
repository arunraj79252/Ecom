//package com.assessment.ecom.auth;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class CustomIpFilter extends OncePerRequestFilter {
//
//    private final List<String> allowedIps;
//
//    public CustomIpFilter(@Value("${allowed.ips}") String allowedIpsProperty) {
//        this.allowedIps = Arrays.asList(allowedIpsProperty.split(","));
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String clientIp = request.getRemoteAddr();
//        if (!allowedIps.contains(clientIp)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied for IP: " + clientIp);
//            return;
//        }
//        filterChain.doFilter(request, response);
//    }
//}
