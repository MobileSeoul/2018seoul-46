package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class NotMatchException extends ResponseException {

    public NotMatchException() {
        super(ExceptionCode.NOT_MATCH);
    }

    public NotMatchException(String message) {
        super(ExceptionCode.NOT_MATCH, message);
    }

    public NotMatchException(ExceptionCode exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
