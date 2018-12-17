package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class InaccessibleBroadcastException extends ResponseException {

    public InaccessibleBroadcastException() {
        super(ExceptionCode.INACCESSIBLE_BROADCAST);
    }
}
