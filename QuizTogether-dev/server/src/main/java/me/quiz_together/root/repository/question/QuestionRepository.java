package me.quiz_together.root.repository.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.model.question.Question;

@Repository
public class QuestionRepository {
    @Autowired
    private QuestionMapper questionMapper;

    public List<Question> selectQuestionListByBroadcastId(long broadcastId) {
        return questionMapper.selectQuestionListByBroadcastId(broadcastId);
    }

    public Question selectQuestionByQuestionId(long questionId) {
        return questionMapper.selectQuestionByQuestionId(questionId);
    }

    public Question selectQuestionByBroadcastIdAndStep(long broadcastId, int step) {
        return questionMapper.selectQuestionByBroadcastIdAndStep(broadcastId, step);
    }

    public void insertQuestionList(List<Question> questionList) {
        questionMapper.insertQuestionList(questionList);
    }

    public int updateQuestionListByQuestionId(List<Question> questionList) {
        return questionMapper.updateQuestionListByQuestionId(questionList);
    }
}
