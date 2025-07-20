package com.stratumtech.realtyticket.validation.validator;

import com.stratumtech.realtyticket.validation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private Pattern regex;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        this.regex = Pattern.compile(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return regex.matcher(value).matches();
    }
}

