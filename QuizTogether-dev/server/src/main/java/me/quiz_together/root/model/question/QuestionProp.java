package me.quiz_together.root.model.question;

import java.util.List;

import lombok.Data;

@Data
public class QuestionProp {
    private String title;
    private List<String> options;
}
