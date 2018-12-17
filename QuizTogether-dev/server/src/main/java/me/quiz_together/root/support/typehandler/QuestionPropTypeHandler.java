package me.quiz_together.root.support.typehandler;

import org.apache.ibatis.type.MappedTypes;

import me.quiz_together.root.model.question.QuestionProp;

@MappedTypes(QuestionProp.class)
public class QuestionPropTypeHandler extends ObjectJsonTypeHandler<QuestionProp> {
    public QuestionPropTypeHandler(Class<QuestionProp> type) {
        super(type);
    }
}
