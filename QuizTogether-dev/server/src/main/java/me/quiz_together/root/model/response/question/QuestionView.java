package me.quiz_together.root.model.response.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import me.quiz_together.root.model.question.CategoryType;
import me.quiz_together.root.model.question.QuestionProp;
import me.quiz_together.root.support.hashid.HashQuestionId;

@Data
@Builder
public class QuestionView {
    @ApiModelProperty(dataType = "java.lang.String")
    @HashQuestionId
    private Long questionId;
    private int answerNo;
    private int step;
    private QuestionProp questionProp;
    private CategoryType category;
}
