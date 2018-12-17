package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class InvalidCreateException extends ResponseException {

    public InvalidCreateException() {
        super(ExceptionCode.INVALID_CREATE);
    }

    public InvalidCreateException(String message) {
        super(ExceptionCode.INVALID_CREATE, message);
    }
}
