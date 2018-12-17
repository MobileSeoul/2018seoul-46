package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class ConflictUserException extends ResponseException {

    public ConflictUserException() {
        super(ExceptionCode.CONFLICT_USER);
    }
}
