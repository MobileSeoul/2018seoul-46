package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class ConflictFollowerException extends ResponseException {

    public ConflictFollowerException() {
        super(ExceptionCode.CONFLICT_FOLLOWER);
    }
}