package me.quiz_together.root.support.hashid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HashQuestionIdConstraintValidator implements ConstraintValidator<HashQuestionId, Long> {
    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null || id >= 0) {
            return true;
        }

        return false;
    }
}
