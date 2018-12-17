package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class FileNameEmptyException extends ResponseException {

    public FileNameEmptyException() {
        super(ExceptionCode.FILE_NAME_EMPTY);
    }
}
