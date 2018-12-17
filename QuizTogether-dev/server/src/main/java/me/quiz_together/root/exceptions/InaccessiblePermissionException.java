package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class InaccessiblePermissionException extends ResponseException {
    public InaccessiblePermissionException() {
        super(ExceptionCode.INACCESSIBLE_PERMISSION);
    }
}
