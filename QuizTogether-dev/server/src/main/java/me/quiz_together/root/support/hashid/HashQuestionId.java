package me.quiz_together.root.support.hashid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.quiz_together.root.support.HashIdUtils.HashIdType;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {HashQuestionIdConstraintValidator.class}
)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@HashId(HashIdType.QUESTION_ID)
@JacksonAnnotationsInside
@JsonDeserialize(using = HashQuestionIdDeserializer.class)
@JsonSerialize(using = HashQuestionIdSerializer.class)
public @interface HashQuestionId {
    String message() default "invalidBroadcastId";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
