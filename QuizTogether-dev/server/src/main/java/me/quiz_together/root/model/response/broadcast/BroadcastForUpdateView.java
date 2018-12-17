package me.quiz_together.root.model.response.broadcast;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import me.quiz_together.root.model.broadcast.GiftType;
import me.quiz_together.root.model.response.question.QuestionView;
import me.quiz_together.root.support.hashid.HashBroadcastId;

@Data
@Builder
public class BroadcastForUpdateView {
    @ApiModelProperty(dataType = "java.lang.String")
    @HashBroadcastId
    private Long broadcastId;
    private String title;
    private String description;
    private Long scheduledTime;
    private GiftType giftType;
    private Long prize;
    private String giftDescription;
    private String winnerMessage;
    private List<QuestionView> questionList;
}
