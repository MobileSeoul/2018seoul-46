package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class AbusingUserException extends ResponseException {

    public AbusingUserException() {
        super(ExceptionCode.ABUSING_USER);
    }

    public AbusingUserException(String message) {
        super(ExceptionCode.ABUSING_USER, message);
    }
}

