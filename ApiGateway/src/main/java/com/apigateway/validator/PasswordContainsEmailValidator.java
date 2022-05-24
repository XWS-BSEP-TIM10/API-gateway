package com.apigateway.validator;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordContainsEmailValidator implements ConstraintValidator<PasswordContainsEmail, Object> {

    private String passwordFieldName;
    private String emailFieldName;
    private String message;

    @Override
    public void initialize(final PasswordContainsEmail constraintAnnotation) {
        passwordFieldName = constraintAnnotation.password();
        emailFieldName = constraintAnnotation.email();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;
        try
        {
            final Object firstObj = BeanUtils.getProperty(value, passwordFieldName);
            final Object secondObj = BeanUtils.getProperty(value, emailFieldName);

            valid = isValid(firstObj, secondObj);
        }
        catch (final Exception ignore)
        {
            // ignore
        }

        if (!valid){
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(passwordFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }

    private boolean isValid(Object firstObj, Object secondObj) {
        return firstObj == null && secondObj == null || firstObj != null &&
                !firstObj.toString().toLowerCase().contains(secondObj.toString().split("@")[0].toLowerCase());
    }
}
