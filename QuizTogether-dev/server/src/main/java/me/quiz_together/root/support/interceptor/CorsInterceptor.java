package me.quiz_together.root.support.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CorsInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!CorsUtils.isCorsRequest(request)) {
            return true;
        }

        String requestOrigin = getRequestOrigin(request);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestOrigin);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.TRUE.toString());
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, OPTIONS");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, Long.toString(3600));

        return true;
    }

    private String getRequestOrigin(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.ORIGIN);
    }
}
