package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class CreatedBroadcastMaxException extends ResponseException {

    public CreatedBroadcastMaxException() {
        super(ExceptionCode.CREATED_BROADCAST_MAX);
    }
}
