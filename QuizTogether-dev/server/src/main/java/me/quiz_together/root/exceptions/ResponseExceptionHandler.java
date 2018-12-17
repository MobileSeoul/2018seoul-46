package me.quiz_together.root.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.supoort.ExceptionCode;
import me.quiz_together.root.model.supoort.ResultContainer;

@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    public final ResultContainer<Object> handleResponseException(HttpServletRequest request, ResponseException e) {
        log.error(e.toString(), e);

        return new ResultContainer<>(e.getExceptionCode(), e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResultContainer<Object> handleException(HttpServletRequest req, Exception exception) {
        log.error(exception.toString(), exception);
        return new ResultContainer<>(ExceptionCode.INTERNAL_SERVER_ERROR, exception.getMessage(), null);
    }

}
