package com.apigateway.validator;

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import com.codahale.passpol.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class BlackListValidator implements ConstraintValidator<BlackList, String> {
    @Override
    public void initialize(BlackList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        List<String> passwords = new ArrayList<>();
        passwords.add("Password1!");
        passwords.add("Password1/");
        passwords.add("Password1@");
        passwords.add("Password1#");
        PasswordPolicy policy = new PasswordPolicy(BreachDatabase.passwordSet(passwords), 10, 64);
        Status result = policy.check(password);
        return result.equals(Status.OK);
    }
}
