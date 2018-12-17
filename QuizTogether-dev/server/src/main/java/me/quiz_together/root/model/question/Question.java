package me.quiz_together.root.model.question;

import lombok.Data;

@Data
public class Question {
    private Long id;
    private Long userId;
    private Long broadcastId;
    private Integer step;
    private QuestionProp questionProp;
    private Integer answerNo;
    private Long createdTime;
    private Long updatedTime;
    private CategoryType category;

}
