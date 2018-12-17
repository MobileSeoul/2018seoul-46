package me.quiz_together.root.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.supoort.ExceptionCode;

@Slf4j
public abstract class ResponseException extends RuntimeException {
    private static final long serialVersionUID = 142912352891637163L;

    @Getter
    protected ExceptionCode exceptionCode = ExceptionCode.INTERNAL_SERVER_ERROR;

    protected ResponseException(ExceptionCode exceptionCode) {
        this(exceptionCode, exceptionCode.getMessage());
    }

    protected ResponseException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
