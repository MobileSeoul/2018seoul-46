package me.quiz_together.root.repository.question;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.quiz_together.root.model.question.Question;

@Mapper
public interface QuestionMapper {
    List<Question> selectQuestionListByBroadcastId(@Param("broadcastId") long broadcastId);

    Question selectQuestionByQuestionId(@Param("questionId") long questionId);

    Question selectQuestionByBroadcastIdAndStep(@Param("broadcastId") long broadcastId, @Param("step")int step);

    void insertQuestionList(List<Question> questionList);

    int updateQuestionListByQuestionId(List<Question> questionList);

}
