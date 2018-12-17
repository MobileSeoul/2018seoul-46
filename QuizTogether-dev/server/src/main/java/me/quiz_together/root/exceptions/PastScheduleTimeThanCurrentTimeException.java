package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class PastScheduleTimeThanCurrentTimeException extends ResponseException {

    public PastScheduleTimeThanCurrentTimeException() {
        super(ExceptionCode.PAST_SCHEDULE_TIME_THAN_CURRENT_TIME);
    }
}