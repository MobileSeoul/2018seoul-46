package me.quiz_together.root.service.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.repository.question.QuestionRepository;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getQuestionListByBroadcastId(long broadcastId) {
        return questionRepository.selectQuestionListByBroadcastId(broadcastId);
    }

    public Question getQuestionByQuestionId(long questionId) {
        return questionRepository.selectQuestionByQuestionId(questionId);
    }

    public Question getQuestionByBroadcastIdAndStep(long broadcastId, int step) {
        return questionRepository.selectQuestionByBroadcastIdAndStep(broadcastId, step);
    }

    public void insertQuestionList(List<Question> questionList) {
        questionRepository.insertQuestionList(questionList);
    }

    public int updateQuestionListByQuestionId(List<Question> questionList) {
        return questionRepository.updateQuestionListByQuestionId(questionList);
    }
}
