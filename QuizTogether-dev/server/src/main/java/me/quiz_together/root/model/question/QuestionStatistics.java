package me.quiz_together.root.model.question;

import java.util.Map;

import lombok.Data;

@Data
public class QuestionStatistics {
    Map<Integer, Long> answerNoSum;
}
