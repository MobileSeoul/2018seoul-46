package me.quiz_together.root.support;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.google.common.base.Charsets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServletRequestDumpUtils {

    public static void printHttpServletRequestValue(HttpServletRequest request) {
        log.info(">> Request : {}", request.getRemoteAddr());
        printHttpServletRequestBasicValue(request);
        printHttpServletRequestHeaderValue(request);
        printHttpServletRequestCookieValue(request);
        printHttpServletRequestParameterValue(request);
        printHttpServletRequestBodyValue(request);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }

    private static void printHttpServletRequestBasicValue(HttpServletRequest request) {
        log.info(">> {} {} {}", request.getMethod(), request.getRequestURL(), request.getProtocol());
    }

    private static void printHttpServletRequestHeaderValue(HttpServletRequest request) {
        Enumeration<?> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info(">> HEADER >> {} : {}", headerName, headerValue.trim());
        }
    }

    private static void printHttpServletRequestParameterValue(HttpServletRequest request) {
        Enumeration<?> parametersNames = request.getParameterNames();
        while(parametersNames.hasMoreElements()) {
            String parameterName = (String) parametersNames.nextElement();
            String parameterValue = request.getParameter(parameterName);
            log.info(">> PARAMS >> {} : {}", parameterName, parameterValue);
        }
    }

    private static void printHttpServletRequestBodyValue(HttpServletRequest request) {
        if ( request instanceof ContentCachingRequestWrapper) {
            String requestBody = new String(((ContentCachingRequestWrapper)request).getContentAsByteArray(),
                                            Charsets.UTF_8);
            if(StringUtils.isNotBlank(requestBody)) {
                String[] lines = requestBody.split("[\r\n]+");
                for (String line : lines) {
                    log.info(">> BODY >> {}", line);
                }
            }
        }
    }

    private static void printHttpServletRequestCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : request.getCookies()) {
                log.info(">> COOKIE >> {} : {}", cookie.getName(), cookie.getValue());
            }
        }
    }
}
