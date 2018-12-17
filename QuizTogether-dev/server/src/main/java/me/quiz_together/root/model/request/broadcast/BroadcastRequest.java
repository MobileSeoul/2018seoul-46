package me.quiz_together.root.model.request.broadcast;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.model.broadcast.GiftType;
import me.quiz_together.root.model.request.question.QuestionRequest;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class BroadcastRequest {
    @NotNull
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long userId;
    @NotNull
    private String title;
    @NotNull
    private String description;
    private Long scheduledTime;
    @NotNull
    private GiftType giftType;
    private Long prize;
    private String giftDescription;
    @NotNull
    private String winnerMessage;
    @NotEmpty
    private List<QuestionRequest> questionList;
}

