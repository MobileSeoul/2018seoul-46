package me.quiz_together.root.support;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.support.security.SecurityUtils;

@Slf4j
@Component
public class HttpReqLoggingFilter extends OncePerRequestFilter {

    private static final  String LOG_FORMAT = "%s|%s|%s|%s|%s|%s|%s|%s";
    private static final String SUCC = "SUCC";
    private static final String FAIL = "FAIL";

    private ThreadLocal<Long> startTime = new ThreadLocal<>();
    private ThreadLocal<String> requestId = new ThreadLocal<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Object requestToUse = new ContentCachingRequestWrapper(request);
        beforeRequest((HttpServletRequest)requestToUse);
        try {
            filterChain.doFilter((HttpServletRequest)requestToUse, response);
        } catch (Exception e) {
            log.error("unhandledException", e);
            response.setStatus(500);
        } finally {
            afterRequest((HttpServletRequest)requestToUse, response);
        }

    }

    private String generateMDCValue() {
        long currentTimeMillis = System.currentTimeMillis();

        return String.format("%s", SecurityUtils.encryptSHA256(Long.toString(currentTimeMillis)).substring(0,10));
    }

    private void beforeRequest(HttpServletRequest request) {
        startTime.set(System.currentTimeMillis());
        requestId.set(request.getRemoteAddr() + ":" + System.nanoTime());

        MDC.put("tid", generateMDCValue());
    }

    /* Sucess or Failure, Status Code, Request Id, Request End Time, API, API Param, Response Data, Lang, Region, Client IP, Processing Time */
    private void afterRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequestDumpUtils.printHttpServletRequestValue(request);

        //TODO : 실패시에도 200으로 내려오는 경우 발생
        String success = SUCC;
        int statusCode = response.getStatus();
        if (statusCode != 200) {
            success = FAIL;
        }

        LocalDateTime requestEndTime = LocalDateTime.now();
        Long requestIntervalTime = System.currentTimeMillis() - startTime.get();

        String clientIp = request.getRemoteAddr();
        String api = request.getRequestURI();

        String lang = Optional.ofNullable(request.getLocale()).map(Locale::getLanguage).orElse("U");
        String region = Optional.ofNullable(request.getLocale()).map(Locale::getCountry).orElse("U");

        String logStr = String.format(LOG_FORMAT, success, statusCode , requestEndTime, api, "lang-" + lang, region, clientIp, requestIntervalTime + "ms");

        log.info(logStr);
    }
}
