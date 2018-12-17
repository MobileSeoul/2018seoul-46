package me.quiz_together.root.model.supoort;

import org.springframework.http.HttpStatus;

public enum ExceptionCode {
    SUCCESS(HttpStatus.OK),
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST),

    FORBIDDEN(HttpStatus.FORBIDDEN),
    CREATED_BROADCAST_MAX(HttpStatus.FORBIDDEN, "최대 생성 갯수 초과"),
    PAST_SCHEDULE_TIME_THAN_CURRENT_TIME(HttpStatus.FORBIDDEN, "예약 시간은 현재 시간 보다 커야 합니다."),
    INVALID_CREATE(HttpStatus.FORBIDDEN, "invalid create try!!"),
    INACCESSIBLE_BROADCAST(HttpStatus.FORBIDDEN, "현재 접근 불가능한 방입니다."),
    INACCESSIBLE_PERMISSION(HttpStatus.FORBIDDEN, "해당 권한이 없습니다."),
    NOT_MATCH(HttpStatus.FORBIDDEN),
    FILE_NAME_EMPTY(HttpStatus.FORBIDDEN, "file name must be not empty"),
    INVALID_UPDATE(HttpStatus.FORBIDDEN, "invalid update try!!"),
    ABUSING_USER(HttpStatus.FORBIDDEN, "ABUSING_USER!!"),
    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),

    // 409
    CONFLICT_USER(HttpStatus.CONFLICT, "중복된 이름이 존재 합니다."),
    CONFLICT_FOLLOWER(HttpStatus.CONFLICT, "이미 등록한 유저 입니다."),


    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final int code;
    private final String message;

    ExceptionCode(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.code = httpStatus.value();
        this.message = message;
    }

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean equals(ExceptionCode other) {
        if (this == other) {
            return true;
        }
        return code == other.getCode();
    }

    public boolean equals(int code) {
        return this.code == code;
    }
}
