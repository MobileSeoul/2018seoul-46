package me.quiz_together.root.model.firebase;

import lombok.Builder;
import lombok.Getter;
import me.quiz_together.root.model.question.QuestionProp;

@Builder
@Getter
public class QuestionMessage implements FcmMessage {
    private QuestionProp questionProp;
    private int step;
    private PushType pushType;
}
