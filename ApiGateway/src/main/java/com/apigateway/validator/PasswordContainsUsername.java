package com.apigateway.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordContainsUsernameValidator.class)
@Documented
public @interface PasswordContainsUsername {
    String message() default "The password can not contain email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String password();
    String username();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        PasswordContainsUsername[] value();
    }
}