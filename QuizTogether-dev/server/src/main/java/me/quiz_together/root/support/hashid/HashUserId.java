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
        validatedBy = {HashUserIdConstraintValidator.class}
)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@HashId(HashIdType.USER_ID)
@JacksonAnnotationsInside
@JsonDeserialize(using = HashUserIdDeserializer.class)
@JsonSerialize(using = HashUserIdSerializer.class)
public @interface HashUserId {
    String message() default "invalidUserId";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
