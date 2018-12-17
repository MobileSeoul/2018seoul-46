package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class NotFoundUserException extends ResponseException {

    public NotFoundUserException() {
        super(ExceptionCode.NOT_FOUND_USER);
    }
}
