package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class InvalidUpdateException extends ResponseException {

    public InvalidUpdateException() {
        super(ExceptionCode.INVALID_UPDATE);
    }

    public InvalidUpdateException(String message) {
        super(ExceptionCode.INVALID_UPDATE, message);
    }
}
