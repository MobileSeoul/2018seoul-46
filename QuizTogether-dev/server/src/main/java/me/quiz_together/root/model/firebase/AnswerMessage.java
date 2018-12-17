package me.quiz_together.root.model.firebase;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import me.quiz_together.root.model.question.QuestionProp;

@Builder
@Getter
public class AnswerMessage implements FcmMessage {
    private QuestionProp questionProp;
    private int answerNo;
    private int step;
    private Map<Integer, Integer> questionStatistics;
    private PushType pushType;
}
